package me.conclure.eventful.shared;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import me.conclure.eventful.shared.collection.Registry;
import me.conclure.eventful.shared.messaging.*;
import me.conclure.eventful.shared.messaging.service.LettuceMessenger;
import me.conclure.eventful.shared.messaging.service.Messenger;
import me.conclure.eventful.shared.messaging.type.MessageType;
import me.conclure.eventful.shared.messaging.type.MessageTypes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class MainRunner {
    public static void main(String[] args) throws InterruptedException {
        Registry<MessageType<?>, Integer> messageRegistry = Registry.createThreadSafe(MessageType::id);
        MessageTypes.registerTo(messageRegistry);
        Messenger messenger = new LettuceMessenger(RedisClient.create(ClientResources.builder()
                        .build(),
                RedisURI.builder()
                        .withHost("localhost")
                        .withPort(6379)
                .build()));
        messenger.bootUp();
        messenger.subscribe("aaa",reader -> {
            System.out.println(reader.readUTF());
        }).join();
        messenger.publish("aaa",writer -> {
            writer.writeUTF("ddd");
        }).join();
        MessageCenter hub = MessageCenter.builder()
                .signature("hub")
                .messenger(messenger)
                .channel(Identifier.of("test"))
                .registry(messageRegistry)
                .build();
        hub.bootUp();
        MessageCenter game = MessageCenter.builder()
                .signature("game")
                .channel(Identifier.of("test"))
                .messenger(messenger)
                .registry(messageRegistry)
                .build();
        game.bootUp();
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("Epic");
            return thread;
        });
        MessageTypes.INT.register(hub, message -> {
            Integer unwrap = message.unwrap();
            System.out.printf("hub received - %s\n thread - %s\n",unwrap,Thread.currentThread());
        });

        MessageTypes.INT.register(game, message -> {
            Integer unwrap = message.unwrap();
            System.out.printf("game received - %s\n thread - %s\n",unwrap,Thread.currentThread());
        },executor);

        sleep(5000);
        MessageTypes.INT.send(3,hub);
        MessageTypes.INT.send(4,game);
        sleep(5000);
        messenger.terminate();
    }
}