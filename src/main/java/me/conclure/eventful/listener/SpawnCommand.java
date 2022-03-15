package me.conclure.eventful.listener;

import me.conclure.eventful.command.CommandContext;
import me.conclure.eventful.command.VoidedCommandExecutor;
import me.conclure.eventful.model.Mapping;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnCommand implements VoidedCommandExecutor {
    private final Mapping<Location,String> locationMapping;

    public SpawnCommand(Mapping<Location, String> locationMapping) {

        this.locationMapping = locationMapping;
    }

    @Override
    public void execute(CommandContext context) {
        if (!(context.sender() instanceof Player player)) {
            return;
        }

        Location location = this.locationMapping.get("spawn")
                .or(player.getWorld().getSpawnLocation())
                .assertPresent()
                .value();
        player.teleportAsync(location);
    }
}
