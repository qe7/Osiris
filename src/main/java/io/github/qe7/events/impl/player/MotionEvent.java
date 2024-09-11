package io.github.qe7.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public final class MotionEvent {

    private double x, minY, y, z;

    private float yaw, pitch;

    private boolean onGround;
}
