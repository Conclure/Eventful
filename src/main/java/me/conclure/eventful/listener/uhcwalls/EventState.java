package me.conclure.eventful.listener.uhcwalls;

import me.conclure.eventful.listener.EventObserver;
import me.conclure.eventful.nullability.Nilable;
import org.bukkit.entity.Player;

import java.util.UUID;

sealed interface EventState extends EventObserver permits Game, Lobby {
    default Nilable<Lobby> asLobby() {
        return Nilable.absent();
    }

    default Nilable<Game> asGame() {
        return Nilable.absent();
    }

    boolean containsPlayer(UUID uuid);
}