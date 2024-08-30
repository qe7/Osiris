package io.github.qe7.events.impl.render;

import io.github.qe7.events.api.types.Event;
import lombok.Getter;
import net.minecraft.src.ScaledResolution;

@Getter
public class RenderScreenEvent extends Event {

    private final ScaledResolution scaledResolution;

    public RenderScreenEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
