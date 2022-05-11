package me.conclure.eventful.proxy;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;
import me.conclure.eventful.shared.loggin.LoggerAdapter;
import me.conclure.eventful.shared.messaging.MessageCenter;
import me.conclure.eventful.shared.messaging.type.MessageTypes;

public class PingCommand implements CommandSupplier {
    private final MessageCenter messageCenter;
    private final LoggerAdapter logger;

    public PingCommand(MessageCenter messageCenter, LoggerAdapter logger) {
        this.messageCenter = messageCenter;
        this.logger = logger;
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> arguments() {
        return LiteralArgumentBuilder.<CommandSource>literal("ping")
                .then(
                        RequiredArgumentBuilder.<CommandSource,String>argument("message",greedyString())
                                .executes(ctx -> {
                                    String message = ctx.getArgument("message", String.class);
                                    MessageTypes.PING.send(message.split(" "),this.messageCenter).thenRun(() -> {
                                        this.logger.infof("[Messaging] (PING) %s", message);
                                    });
                                    return Command.SINGLE_SUCCESS;
                                })
                );
    }
}
