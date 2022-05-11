package me.conclure.eventful.shared.messaging.type;

import me.conclure.eventful.shared.collection.Registry;
import me.conclure.eventful.shared.event.EventInfo;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

public final class MessageTypes {
    public static final MessageType<String[]> PING;
    public static final MessageType<String> STRING;
    public static final MessageType<Integer> INT;

    static {
        int id = 0;
        PING = new PingMessageType(id++);
        STRING = new StringMessageType(id++);
        INT = new IntMessageType(id++);
    }

    @SuppressWarnings("unchecked")
    public static Stream<MessageType<?>> stream() {
        return Arrays.stream(MessageTypes.class.getDeclaredFields())
                .filter(field -> Modifier.isFinal(field.getModifiers()))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(MessageType.class::cast);
    }

    public static void registerTo(Registry<MessageType<?>,Integer> registry) {
        MessageTypes.stream().forEach(registry::register);
    }
}
