package io.github.qe7.events.impl.player;

import io.github.qe7.events.api.types.Event;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MotionEvent extends Event {

    private double x, minY, y, z;

    private float yaw, pitch;

    private boolean onGround;

    public MotionEvent(double x, double minY, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.minY = minY;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
}
