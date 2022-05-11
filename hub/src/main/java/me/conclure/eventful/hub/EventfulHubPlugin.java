package me.conclure.eventful.hub;

import me.conclure.eventful.hub.bootstrap.HubBootstrap;
import me.conclure.eventful.shared.loggin.Slf4jLoggerAdapter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;

public class EventfulHubPlugin extends JavaPlugin {
    private final HubBootstrap bootstrap = new HubBootstrap(this, new Slf4jLoggerAdapter(this.getSLF4JLogger()));

    @Override
    public void onEnable() {
        try {
            this.bootstrap.enable();
            System.out.println(Paths.get("").toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        this.bootstrap.disable();
    }

    @Override
    public @Nullable PluginCommand getCommand(@NotNull String name) {
        return super.getCommand(name);
    }
}
