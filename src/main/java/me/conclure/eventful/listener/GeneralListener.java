package me.conclure.eventful.listener;

import me.conclure.eventful.bootstrap.EventfulJavaPlugin;
import me.conclure.eventful.model.Mapping;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class GeneralListener implements Listener {
    private final Mapping<Location,String> locationMapping;
    private final EventController eventController;

    public GeneralListener(Mapping<Location, String> locationMapping, EventController eventController) {
        this.locationMapping = locationMapping;
        this.eventController = eventController;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (this.eventController.isPlayerInEvent(player.getUniqueId())) {
            return;
        }
        Location location = this.locationMapping.get("spawn")
                .orValue(player.getWorld().getSpawnLocation());
        player.teleportAsync(location);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Bukkit.getScheduler().runTaskLater(EventfulJavaPlugin.getInstance(),() -> {
            player.spigot().respawn();
            Location location = this.locationMapping.get("spawn")
                    .orValue(player.getWorld().getSpawnLocation());
            player.teleportAsync(location);
        }, 10L);
    }
}