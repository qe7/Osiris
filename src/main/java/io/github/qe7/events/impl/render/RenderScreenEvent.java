package io.github.qe7.events.impl.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.src.ScaledResolution;

@Getter
@AllArgsConstructor
public final class RenderScreenEvent {

    private final ScaledResolution scaledResolution;
}
