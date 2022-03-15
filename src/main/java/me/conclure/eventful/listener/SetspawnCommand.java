package me.conclure.eventful.listener;

import me.conclure.eventful.command.CommandContext;
import me.conclure.eventful.command.VoidedCommandExecutor;
import me.conclure.eventful.model.Mapping;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetspawnCommand implements VoidedCommandExecutor {
    private final Mapping<Location,String> locationMapping;

    public SetspawnCommand(Mapping<Location, String> locationMapping) {
        this.locationMapping = locationMapping;
    }

    @Override
    public void execute(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            return;
        }
        this.locationMapping.set("spawn",player.getLocation());
    }
}
