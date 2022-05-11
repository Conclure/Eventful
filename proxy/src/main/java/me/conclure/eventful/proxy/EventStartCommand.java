package me.conclure.eventful.proxy;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class EventStartCommand implements CommandSupplier {
    @Override
    public LiteralArgumentBuilder<CommandSource> arguments() {
        return LiteralArgumentBuilder.<CommandSource>literal("start").executes(context -> {
            CommandSource source = context.getSource();
            source.sendMessage(Component.text("Hi"));
            return Command.SINGLE_SUCCESS;
        });
    }
}
