package me.conclure.eventful.listener.uhcwalls;

import com.google.common.collect.ImmutableSet;
import me.conclure.eventful.nullability.Nil;
import me.conclure.eventful.utility.Assertion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

final class Lobby implements EventState {
    private static final int MIN_COUNT = 4;

    private final Set<UUID> players;
    private final Location location;
    private boolean isValid;

    Lobby(Location location) {
        this.location = location;
        this.players = new HashSet<>(1);
        this.isValid = true;
    }

    boolean hasEnoughPlayers() {
        return this.players.size() >= MIN_COUNT;
    }

    @Override
    public Nil<Lobby> asLobby() {
        return Nil.present(this);
    }

    @Override
    public boolean containsPlayer(UUID uuid) {
        return this.isValid && this.players.contains(uuid);
    }

    void teleportAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleportAsync(this.location);
        }
    }

    void checkValidity() {
        if (!this.isValid) {
            Assertion.raise();
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.checkValidity();
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        this.checkValidity();

        Player player = event.getPlayer();

        this.players.add(player.getUniqueId());
        player.teleportAsync(this.location);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.checkValidity();

        Player player = event.getPlayer();
        this.players.remove(player.getUniqueId());
    }

    void invalidate() {
        this.checkValidity();

        this.isValid = false;
        this.players.clear();
    }

    ImmutableSet<UUID> players() {
        return ImmutableSet.copyOf(this.players);
    }
}