package me.conclure.eventful.proxy;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.conclure.eventful.shared.event.EventInfo;
import me.conclure.eventful.shared.messaging.MessageCenter;
import me.conclure.eventful.shared.nullability.Nil;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventManager {
    private EventInfo currentEvent;
    private final MessageCenter center;
    private final Executor executor;
    private final ProxyServer server;
    public EventManager(MessageCenter center, ProxyServer server) {
        this.center = center;
        this.server = server;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void bootUp() {
    }
}