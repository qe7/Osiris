package io.github.qe7.features.waypoints.enums;

public enum WaypointDimension {
    OVER_WORLD(0),
    NETHER(1),
    END(2);

    private final int id;

    WaypointDimension(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
