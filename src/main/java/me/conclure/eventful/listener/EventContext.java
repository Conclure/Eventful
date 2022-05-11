package me.conclure.eventful.listener;

import me.conclure.eventful.nullability.Nil;
import org.bukkit.World;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class EventContext {
    private final Reference<World> world;

    public EventContext(World world) {
        this.world = new WeakReference<>(world);
    }

    public static EventContext.Builder builder() {
        return new Builder();
    }

    public Nil<World> world() {
        return Nil.optional(this.world.get());
    }

    static class Builder {
        private World world;

        public Builder world(World world) {
            this.world = world;
            return this;
        }

        public EventContext build() {
            return new EventContext(this.world);
        }
    }
}
