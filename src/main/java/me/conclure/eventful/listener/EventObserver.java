package me.conclure.eventful.listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface EventObserver {

    default void onPlayerJoin(PlayerJoinEvent event) {}

    default void onPlayerLogin(PlayerLoginEvent event) {}

    default void onPlayerQuit(PlayerQuitEvent event) {}
}
