package me.conclure.eventful.listener.uhcwalls;

import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Material.AIR;

public class Team {
    private final Set<UUID> players;

    public Team() {
        this.players = new HashSet<>();
    }

    void add(UUID uniqueId) {
        this.players.add(uniqueId);
    }

    void remove(UUID uniqueId) {
        this.players.remove(uniqueId);
    }

    void clear() {
        this.players.clear();
    }
}
