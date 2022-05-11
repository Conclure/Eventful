package me.conclure.eventful.bootstrap;

import me.conclure.eventful.EventCommandExecutor;
import me.conclure.eventful.listener.*;
import me.conclure.eventful.listener.uhcwalls.EventUHCWalls;
import me.conclure.eventful.model.Mapping;
import me.conclure.eventful.model.MappingImpl;
import me.conclure.eventful.model.Registry;
import me.conclure.eventful.nullability.Nil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class EventfulJavaPlugin extends JavaPlugin {
    static EventfulJavaPlugin instance;

    public static EventfulJavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        System.out.println("${COMPILE_TIME}");
        Mapping<Location,String> locationMapping = new MappingImpl<>(new HashMap<>()){
            @Override
            public Nil<Location> get(String id) {
                if (id.equals("uhcwalls")) {
                    return Nil.present(new Location(Bukkit.getWorlds().get(0),0,0,0));
                }
                return super.get(id);
            }
        };
        EventManager eventManager = new EventManager();
        Registry<Event, String> eventRegistry = Registry.create(Event::id);
        eventRegistry.register(new EventUHCWalls("uhcwalls", locationMapping));


        this.setCommandExecutor("event",new EventCommandExecutor(null));
        this.setCommandExecutor("eventstart", new EventstartCommand(eventManager, eventRegistry));
        this.setCommandExecutor("setspawn",new SetspawnCommand(locationMapping));
        this.setCommandExecutor("spawn", new SpawnCommand(locationMapping));

        this.registerListener(new GeneralListener(locationMapping, eventManager));
        this.registerListener(eventManager);
    }

    private void setCommandExecutor(String command, CommandExecutor executor) {
        Nil.optional(this.getCommand(command))
                .assertPresent()
                .ifPresent(cmd -> cmd.setExecutor(executor));
    }

    private void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener,this);
    }
}

