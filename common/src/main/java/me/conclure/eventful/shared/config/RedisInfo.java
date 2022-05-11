package me.conclure.eventful.shared.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import me.conclure.eventful.shared.nullability.Nil;

public record RedisInfo(int port, String address,
                        Nil<? extends CharSequence> password,
                        Nil<String> username, boolean isSSL) {

    public static RedisInfo create(int port, String address, Nil<String> password, Nil<String> username, boolean ssl
    ) {
        return new RedisInfo(port, address, password, username, ssl);
    }
}
