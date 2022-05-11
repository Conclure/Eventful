package me.conclure.eventful.proxy;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;

public interface CommandSupplier {
    default BrigadierCommand getCommand() {
        return new BrigadierCommand(this.arguments());
    }

    LiteralArgumentBuilder<CommandSource> arguments();
}
