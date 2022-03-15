package me.conclure.eventful;

import me.conclure.eventful.command.CommandArguments;
import me.conclure.eventful.command.CommandContext;
import me.conclure.eventful.command.VoidedCommandExecutor;
import me.conclure.eventful.internalinterface.EventfulInterface;

public class EventCommandExecutor implements VoidedCommandExecutor {
    private final EventfulInterface eventfulInterface;

    public EventCommandExecutor(EventfulInterface eventfulInterface) {
        this.eventfulInterface = eventfulInterface;
    }

    @Override
    public void execute(CommandContext context) {
        CommandArguments arguments = context.arguments();
        if (!arguments.hasIndex(0)) {
            return;
        }
        String firstArgument = arguments.get(0);

    }
}
