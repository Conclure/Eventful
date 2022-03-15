package me.conclure.eventful.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface VoidedCommandExecutor extends CommandExecutor {
    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.execute(CommandContext.create(sender,CommandArguments.create(args),command,label));
        return true;
    }

    void execute(CommandContext context);

    default VoidedCommandExecutor append(VoidedCommandExecutor other) {
        return ctx -> {
            this.execute(ctx);
            other.execute(ctx);
        };
    }

    default VoidedCommandExecutor prepend(VoidedCommandExecutor other) {
        return ctx -> {
            other.execute(ctx);
            this.execute(ctx);
        };
    }
}
