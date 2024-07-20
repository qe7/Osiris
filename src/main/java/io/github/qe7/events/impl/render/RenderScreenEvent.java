package io.github.qe7.events.impl.render;

import io.github.qe7.events.api.types.Event;
import net.minecraft.src.ScaledResolution;

public class RenderScreenEvent extends Event {

    private final ScaledResolution scaledResolution;

    public RenderScreenEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
