package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.features.waypoints.Waypoint;
import io.github.qe7.features.waypoints.enums.WaypointDimension;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.configs.FileUtil;

public final class WaypointManager extends Manager<Waypoint, String> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void initialise() {
        this.loadWaypoints();

        System.out.println("WaypointManager initialised!");
    }

    public void register(Waypoint waypoint) {
        this.getMap().put(waypoint, waypoint.getName());
    }

    public void addWaypoint(String name, String server, WaypointDimension dimension, int x, int y, int z) {
        Waypoint waypoint = new Waypoint(name, server, dimension, x, y, z);

        this.register(waypoint);

        this.saveWaypoints();
    }

    public void removeWaypoint(String name) {
        this.getMap().entrySet().removeIf(entry -> entry.getKey().getName().equalsIgnoreCase(name));

        this.saveWaypoints();
    }

    public void saveWaypoints() {
        JsonObject object = new JsonObject();

        this.getMap().forEach((waypoint, name) -> object.add(name, waypoint.serialize()));

        FileUtil.writeFile("waypoints", GSON.toJson(object));
    }

    public void loadWaypoints() {
        String config = FileUtil.readFile("waypoints");

        if (config == null) {
            return;
        }

        JsonObject object = GSON.fromJson(config, JsonObject.class);

        object.entrySet().forEach(entry -> {
            final String name = entry.getKey();
            final String server = entry.getValue().getAsJsonObject().get("server").getAsString();
            final WaypointDimension dimension = WaypointDimension.valueOf(entry.getValue().getAsJsonObject().get("dimension").getAsString());
            final int x = entry.getValue().getAsJsonObject().get("x").getAsInt();
            final int y = entry.getValue().getAsJsonObject().get("y").getAsInt();
            final int z = entry.getValue().getAsJsonObject().get("z").getAsInt();

            final Waypoint waypoint = new Waypoint(name, server, dimension, x, y, z);

            this.register(waypoint);
        });
    }
}
