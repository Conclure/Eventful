package me.conclure.eventful.listener.uhcwalls;

import com.google.common.collect.ImmutableMap;
import me.conclure.eventful.utility.Assertion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {
    private final Map<UUID,Team> teamView;
    private final ImmutableMap<String,Team> name;

    public TeamManager() {
        this.teamView = new HashMap<>();
        this.name = ImmutableMap.<String, Team>builder()
                .put("blue",new Team())
                .put("red",new Team())
                .put("yellow",new Team())
                .put("green",new Team())
                .build();
    }

    public void add(String team, UUID uuid) {
        Team theTeam = this.name.get(team);
        if (theTeam == null) {
            Assertion.raise();
        }
        this.teamView.put(uuid,theTeam);
        theTeam.add(uuid);
    }

    public void randomlyAdd(UUID uuid) {

    }

    public void randomlyAddAll(Iterable<? extends UUID> uuids) {
        for (UUID uuid : uuids) {
            this.randomlyAdd(uuid);
        }
    }

    public void remove(UUID uuid) {
        Team team = this.teamView.remove(uuid);
        if (team != null) {
            team.remove(uuid);
        }
    }
}