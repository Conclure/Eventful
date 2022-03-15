package me.conclure.eventful.listener;

import me.conclure.eventful.command.CommandArguments;
import me.conclure.eventful.command.CommandContext;
import me.conclure.eventful.command.VoidedCommandExecutor;
import me.conclure.eventful.model.Registry;
import me.conclure.eventful.nullability.Nilable;

public class EventstartCommand implements VoidedCommandExecutor {
    private final EventController eventController;
    private final Registry<Event,String> eventRegistry;

    public EventstartCommand(EventController eventController, Registry<Event, String> eventRegistry) {
        this.eventController = eventController;
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void execute(CommandContext context) {
        CommandArguments arguments = context.arguments();
        if (!arguments.hasIndex(0)) {
            context.reply(0);
            return;
        }
        String firstArgument = arguments.get(0);
        Nilable<Event> eventNilable = this.eventRegistry.get(firstArgument);
        if (eventNilable.isAbsent()) {
            context.reply(1);
            return;
        }
        Event event = eventNilable.assertPresent().value();
        this.eventController.initialize(event);
    }
}