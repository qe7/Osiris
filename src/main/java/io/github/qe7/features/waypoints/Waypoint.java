package io.github.qe7.features.waypoints;

import com.google.gson.JsonObject;
import io.github.qe7.features.waypoints.enums.WaypointDimension;
import io.github.qe7.utils.configs.Serialized;
import lombok.Getter;

@Getter
public class Waypoint implements Serialized {

    private final String name, server;

    private final WaypointDimension dimension;

    private int x, y, z;

    public Waypoint(final String name, final String server, final WaypointDimension dimension, final int x, final int y, final int z) {
        this.name = name;
        this.server = server;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("server", this.server);
        object.addProperty("dimension", this.dimension.name());
        object.addProperty("x", this.x);
        object.addProperty("y", this.y);
        object.addProperty("z", this.z);

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.x = object.get("x").getAsInt();
        this.y = object.get("y").getAsInt();
        this.z = object.get("z").getAsInt();
    }
}
