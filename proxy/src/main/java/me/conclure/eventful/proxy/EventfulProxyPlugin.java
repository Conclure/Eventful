package me.conclure.eventful.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.conclure.eventful.shared.collection.Registry;
import me.conclure.eventful.shared.configuration.ConfigurationFactory;
import me.conclure.eventful.shared.configuration.DefaultConfiguration;
import me.conclure.eventful.shared.configuration.PathedConfigurationFactory;
import me.conclure.eventful.shared.loggin.LoggerAdapter;
import me.conclure.eventful.shared.loggin.Slf4jLoggerAdapter;
import me.conclure.eventful.shared.messaging.MessageCenter;
import me.conclure.eventful.shared.messaging.impl.PingMessageObserver;
import me.conclure.eventful.shared.messaging.service.LettuceMessenger;
import me.conclure.eventful.shared.messaging.service.Messenger;
import me.conclure.eventful.shared.messaging.type.MessageType;
import me.conclure.eventful.shared.messaging.type.MessageTypes;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "eventful", name = "Eventful", version = "1.0-SNAPSHOT", authors = {"Conclure"})
public class EventfulProxyPlugin {
    private final Registry<MessageType<?>,Integer> messageRegistry;
    private final LoggerAdapter logger;
    private DefaultConfiguration configuration;
    private MessageCenter messageCenter;
    private final Path dataDirectory;
    private final ProxyServer server;
    private final ProxyBootstrap bootstrap = new ProxyBootstrap();

    @Inject
    public EventfulProxyPlugin(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path dataDirectory
    ) {
        this.server = server;
        this.logger = new Slf4jLoggerAdapter(logger);
        this.messageRegistry = createMessageRegistry();
        this.dataDirectory = dataDirectory;
    }

    private static Registry<MessageType<?>,Integer> createMessageRegistry() {
        Registry<MessageType<?>,Integer> registry = Registry.createThreadSafe(MessageType::id);
        MessageTypes.registerTo(registry);
        return registry;
    }

    private void loadConfiguration() throws Exception {
        Path configFile = this.dataDirectory.resolve("config.yml");
        PathedConfigurationFactory factory = new PathedConfigurationFactory(configFile, ConfigurationFactory.GSON);
        this.configuration = new DefaultConfiguration(this.logger, factory);
        this.configuration.load();
    }

    private void setupMessageService() {
        Messenger messenger = LettuceMessenger.create(this.configuration.redis());
        messenger.bootUp();
        this.messageCenter = MessageCenter.builder()
                .messenger(messenger)
                .logger(this.logger)
                .fromMessagingInfo(this.configuration.messaging())
                .registry(this.messageRegistry)
                .build();
        this.messageCenter.bootUp();
        this.messageCenter.register(MessageTypes.PING,new PingMessageObserver(this.logger));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.bootstrap.enable();
        try {
            this.loadConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setupMessageService();
        this.registerCommand(new EventStartCommand());
        this.registerCommand(new PingCommand(this.messageCenter, this.logger));
    }

    private void registerCommand(CommandSupplier supplier) {
        this.server.getCommandManager().register(supplier.getCommand());
    }
}
