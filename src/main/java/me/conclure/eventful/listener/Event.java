package me.conclure.eventful.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public interface Event extends EventObserver {
    String id();

    Result initialize();

    Result start();

    boolean containsPlayer(UUID uniqueId);

    interface Result {
        Result FAIL = () -> false;
        Result SUCCESS = () -> true;

        boolean hasSucceeded();

        default boolean hasFailed() {
            return !this.hasSucceeded();
        }
    }
}