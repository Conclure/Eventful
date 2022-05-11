package me.conclure.eventful.shared.config;

import me.conclure.eventful.shared.nullability.Nil;

public record ConfigInfo(MessagingInfo messagingInfo,
                         Nil<RedisInfo> redisInfo) {

    static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MessagingInfo messagingInfo;
        private Nil<RedisInfo> redisInfo;

        public Builder messagingInfo(MessagingInfo messagingInfo) {
            this.messagingInfo = messagingInfo;
            return this;
        }

        public Builder setRedisInfo(Nil<RedisInfo> redisInfo) {
            this.redisInfo = redisInfo;
            return this;
        }

        public ConfigInfo build() {
            return new ConfigInfo(this.messagingInfo, this.redisInfo);
        }
    }
}
