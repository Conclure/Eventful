package me.conclure.eventful.listener.uhcwalls;

import me.conclure.eventful.listener.EventObserver;
import me.conclure.eventful.nullability.Nil;

import java.util.UUID;

sealed interface EventState extends EventObserver permits Game, Lobby {
    default Nil<Lobby> asLobby() {
        return Nil.absent();
    }

    default Nil<Game> asGame() {
        return Nil.absent();
    }

    boolean containsPlayer(UUID uuid);
}