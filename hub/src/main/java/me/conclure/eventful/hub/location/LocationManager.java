package me.conclure.eventful.hub.location;

import me.conclure.eventful.shared.collection.Mapping;
import me.conclure.eventful.shared.collection.Repository;
import me.conclure.eventful.shared.nullability.Nil;
import org.bukkit.Location;

public class LocationManager {
    private final Mapping<Location,String> mapping;

    public LocationManager() {
        this.mapping = Mapping.createThreadSafe();
    }

    public Nil<Location> spawn() {
        return this.mapping.get("spawn");
    }

    public void setSpawn(Location location) {
        this.mapping.set("spawn",location);
    }

    public void unsetSpawn() {
        this.mapping.remove("spawn");
    }
}
