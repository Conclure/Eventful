package me.conclure.eventful.listener.uhcwalls;

import me.conclure.eventful.listener.Event;
import me.conclure.eventful.model.Mapping;
import me.conclure.eventful.nullability.Nilable;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventUHCWalls implements Event {
    private EventState eventState;
    private final String id;
    private final Mapping<Location,String> locationMapping;

    public EventUHCWalls(String id, Mapping<Location,String> locationMapping) {
        this.id = id;
        this.locationMapping = locationMapping;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Result initialize() {
        boolean isEventAlreadyRunning = this.eventState != null;
        if (isEventAlreadyRunning) {
            return Result.FAIL;
        }
        Nilable<Location> locationNilable = this.locationMapping.get(this.id);

        if (locationNilable.isAbsent()) {
            return Result.FAIL;
        }

        Location location = locationNilable.assertPresent().value();
        Lobby lobby = new Lobby(location);
        lobby.teleportAll();
        this.eventState = lobby;
        return Result.SUCCESS;
    }

    @Override
    public Result start() {
        Nilable<Lobby> lobbyNilable = this.eventState.asLobby();
        boolean isLobbyAbsent = lobbyNilable.isAbsent();

        if (isLobbyAbsent) {
            return Result.FAIL;
        }

        Lobby lobby = lobbyNilable.assertPresent().value();

        if (!lobby.hasEnoughPlayers()) {
            return Result.FAIL;
        }

        Game game = new Game();
        game.transferDataFrom(lobby);
        game.teleportAll();

        lobby.invalidate();
        this.eventState = game;

        return Result.SUCCESS;
    }

    @Override
    public boolean containsPlayer(UUID uniqueId) {
        boolean isNotRunning = this.eventState == null;
        if (isNotRunning) {
            return false;
        }

        return this.eventState.containsPlayer(uniqueId);
    }

    private <E extends org.bukkit.event.Event> void consumeEvent(E event,Runnable runnable) {
        if (this.eventState == null) {
            return;
        }
        runnable.run();
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.consumeEvent(event,() -> this.eventState.onPlayerQuit(event));
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.consumeEvent(event,() -> this.eventState.onPlayerJoin(event));
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        this.consumeEvent(event,() -> this.eventState.onPlayerLogin(event));
    }
}