package io.github.qe7.events.impl.render;

import lombok.AllArgsConstructor;
import me.zero.alpine.event.CancellableEvent;
import lombok.Getter;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderManager;

@Getter
@AllArgsConstructor
public final class RenderLivingLabelEvent extends CancellableEvent {

    private final EntityLiving entity;

    private final RenderManager renderManager;

    private final double x, y, z;
}
