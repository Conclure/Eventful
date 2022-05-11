package me.conclure.eventful.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class EventManager implements EventObserver, Listener {
    private Event currentEvent;
    private Reference<World> world = new WeakReference<>(Bukkit.getWorld("world"));

    public Event currentEvent() {
        return this.currentEvent;
    }

    public boolean isEventRunning() {
        return this.currentEvent != null;
    }

    public boolean isPlayerInEvent(UUID uniqueId) {
        return this.isEventRunning() && this.currentEvent.containsPlayer(uniqueId);
    }

    public void world(World world) {
        this.world = new WeakReference<>(world);
    }

    public boolean initialize(Event event) {
        World world = this.world.get();
        if (world == null) {
            return false;
        }
        EventContext context = EventContext.builder()
                .world(world)
                .build();
        Event.Result result = event.initialize(context);
        if (result.hasFailed()) {
            return false;
        }
        this.currentEvent = event;
        return true;
    }

    private void runIfHasCurrentEvent(Runnable runnable) {
        if (this.currentEvent == null) {
            return;
        }
        runnable.run();
    }

    @EventHandler
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.runIfHasCurrentEvent(() -> this.currentEvent.onPlayerQuit(event));
    }

    @EventHandler
    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        this.runIfHasCurrentEvent(() -> this.currentEvent.onPlayerLogin(event));
    }

    @EventHandler
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.runIfHasCurrentEvent(() -> this.currentEvent.onPlayerJoin(event));
    }
}
