package me.conclure.eventful.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandContext {
    static CommandContext create(CommandSender sender, CommandArguments arguments, Command command, String label) {
        return new CommandContextImpl(sender, arguments, command, label);
    }

    CommandSender sender();

    CommandArguments arguments();

    Command command();

    String label();

    void reply(Component component);

    void reply(String string);

    default void reply(int i) {
        this.reply(Component.text(i));
    }
}
