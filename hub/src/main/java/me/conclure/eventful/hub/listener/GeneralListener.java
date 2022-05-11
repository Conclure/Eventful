package me.conclure.eventful.hub.listener;

import me.conclure.eventful.hub.location.LocationManager;
import me.conclure.eventful.shared.nullability.Nil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class GeneralListener implements Listener {
    boolean isLoginAllowed;
    private final LocationManager locationManager;

    public GeneralListener(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        this.locationManager.spawn().ifPresent(player::teleportAsync);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.locationManager.spawn().ifAbsent(() -> {
                    player.sendMessage("eventful.spawn.obstructed");
        });
        player.setGameMode(GameMode.ADVENTURE);
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(attribute);
        for (AttributeModifier modifier : attribute.getModifiers()) {
            attribute.removeModifier(modifier);
        }
        attribute.setBaseValue(2d);
    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Nil<Location> spawn = this.locationManager.spawn();
        spawn.ifPresent(player::teleportAsync);

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(this.getClass()),() -> {
            player.spigot().respawn();
            spawn.ifAbsent(() -> {
                player.sendMessage("eventful.spawn.obstructed");
            });
            player.setGameMode(GameMode.ADVENTURE);
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            Objects.requireNonNull(attribute);
            for (AttributeModifier modifier : attribute.getModifiers()) {
                attribute.removeModifier(modifier);
            }
            attribute.setBaseValue(2d);
        }, 10L);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            return;
        }
        event.setCancelled(true);


    }
}
