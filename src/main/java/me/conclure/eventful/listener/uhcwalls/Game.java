package me.conclure.eventful.listener.uhcwalls;

import com.google.common.collect.ImmutableSet;
import me.conclure.eventful.nullability.Nil;
import me.conclure.eventful.utility.Assertion;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

final class Game implements EventState {
    private boolean isValid;
    private final Set<UUID> players;
    private final TeamManager teamManager;

    Game() {
        this.players = new HashSet<>();
        this.isValid = true;
        this.teamManager = new TeamManager();
    }

    void teleportAll() {

    }

    void transferDataFrom(Lobby lobby) {
        ImmutableSet<UUID> players = lobby.players();
        this.players.addAll(players);
        this.teamManager.randomlyAddAll(players);
    }

    @Override
    public Nil<Game> asGame() {
        return Nil.present(this);
    }

    @Override
    public boolean containsPlayer(UUID uuid) {
        return this.isValid && this.players.contains(uuid);
    }

    void checkValidity() {
        if (!this.isValid) {
            Assertion.raise();
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.checkValidity();
        UUID uniqueId = event.getPlayer().getUniqueId();
        this.players.remove(uniqueId);
        this.teamManager.remove(uniqueId);
    }
}
