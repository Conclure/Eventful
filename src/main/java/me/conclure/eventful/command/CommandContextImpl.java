package me.conclure.eventful.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandContextImpl implements CommandContext{
    private final CommandSender sender;
    private final CommandArguments arguments;
    private final Command command;
    private final String label;

    public CommandContextImpl(CommandSender sender, CommandArguments arguments, Command command, String label) {
        this.sender = sender;
        this.arguments = arguments;
        this.command = command;
        this.label = label;
    }

    @Override
    public CommandSender sender() {
        return this.sender;
    }

    @Override
    public CommandArguments arguments() {
        return this.arguments;
    }

    @Override
    public Command command() {
        return this.command;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public void reply(Component component) {
        this.sender.sendMessage(component);
    }

    @Override
    public void reply(String string) {
        this.sender.sendMessage(string);
    }
}
