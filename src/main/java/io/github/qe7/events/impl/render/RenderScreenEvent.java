package io.github.qe7.events.impl.render;

import lombok.Getter;
import net.minecraft.src.ScaledResolution;

@Getter
public class RenderScreenEvent {

    private final ScaledResolution scaledResolution;

    public RenderScreenEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
