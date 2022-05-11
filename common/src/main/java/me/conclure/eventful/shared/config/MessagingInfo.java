package me.conclure.eventful.shared.config;

import me.conclure.eventful.shared.Identifier;

public record MessagingInfo(String type, String signature, Identifier channel) {
}
