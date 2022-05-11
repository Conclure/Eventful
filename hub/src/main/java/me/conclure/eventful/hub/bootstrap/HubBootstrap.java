package me.conclure.eventful.hub.bootstrap;

import me.conclure.eventful.hub.listener.GeneralListener;
import me.conclure.eventful.hub.location.LocationManager;
import me.conclure.eventful.shared.collection.Registry;
import me.conclure.eventful.shared.configuration.ConfigurationFactory;
import me.conclure.eventful.shared.configuration.DefaultConfiguration;
import me.conclure.eventful.shared.configuration.PathedConfigurationFactory;
import me.conclure.eventful.shared.loggin.LoggerAdapter;
import me.conclure.eventful.shared.messaging.MessageCenter;
import me.conclure.eventful.shared.messaging.service.LettuceMessenger;
import me.conclure.eventful.shared.messaging.service.Messenger;
import me.conclure.eventful.shared.messaging.type.MessageType;
import me.conclure.eventful.shared.messaging.type.MessageTypes;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class HubBootstrap {
    private final JavaPlugin plugin;
    private final Registry<MessageType<?>,Integer> messageRegistry;
    private final LoggerAdapter logger;
    private Messenger messenger;
    private DefaultConfiguration configuration;
    private MessageCenter messageCenter;

    public HubBootstrap(JavaPlugin plugin, LoggerAdapter logger) {
        this.plugin = plugin;
        this.logger = logger;
        this.messageRegistry = createMessageRegistry();
    }

    private static Registry<MessageType<?>,Integer> createMessageRegistry() {
        Registry<MessageType<?>,Integer> registry = Registry.createThreadSafe(MessageType::id);
        MessageTypes.registerTo(registry);
        return registry;
    }

    private Path dataDirectory() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    private void loadConfiguration() throws Exception {
        Path configFile = this.dataDirectory().resolve("config.yml");
        PathedConfigurationFactory factory = new PathedConfigurationFactory(configFile, ConfigurationFactory.GSON);
        this.configuration = new DefaultConfiguration(this.logger, factory);
        this.configuration.load();
    }

    private void setupMessageService() {
        this.messenger = LettuceMessenger.create(this.configuration.redis());
        this.messenger.bootUp();
        this.messageCenter = MessageCenter.builder()
                .messenger(this.messenger)
                .fromMessagingInfo(this.configuration.messaging())
                .registry(this.messageRegistry)
                .build();
        this.messageCenter.bootUp();
        this.messageCenter.register(MessageTypes.PING,message -> {
            String signature = message.senderSignature();
            String content = String.join(" ", message.unwrap());
            this.logger.infof("[Messaging] (PONG f/%s) %s", signature, content);
        });
    }

    public void enable() throws Exception {
        this.loadConfiguration();
        this.setupMessageService();
        PluginCommand ping = this.plugin.getCommand("ping");
        ping.setExecutor((sender,cmd,label,args) -> {
            MessageTypes.PING.send(args,this.messageCenter).thenRun(() -> {
                String message = String.join(" ",args);
                this.logger.infof("[Messaging] (PING) %s", message);
            });
            return true;
        });

        this.registerBukkitListeners();
    }

    private void registerBukkitListeners() {
        LocationManager locationManager = new LocationManager();
        PluginManager pluginManager = this.plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new GeneralListener(locationManager),this.plugin);
    }

    public void disable() {
        this.messenger.terminate();
    }
}
