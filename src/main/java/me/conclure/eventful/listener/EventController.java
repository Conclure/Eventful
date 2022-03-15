package me.conclure.eventful.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventController implements EventObserver, Listener {
    private Event currentEvent;

    public Event currentEvent() {
        return this.currentEvent;
    }

    public boolean isEventRunning() {
        return this.currentEvent != null;
    }

    public boolean isPlayerInEvent(UUID uniqueId) {
        return this.isEventRunning() && this.currentEvent.containsPlayer(uniqueId);
    }

    public void initialize(Event event) {
        Event.Result result = event.initialize();
        if (result.hasFailed()) {
            return;
        }
        this.currentEvent = event;
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
