package io.github.qe7.events.impl.render;

import io.github.qe7.events.api.types.CancellableEvent;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderManager;

public class RenderLivingLabelEvent extends CancellableEvent {

    private final EntityLiving entity;

    private final RenderManager renderManager;

    private final double x, y, z;

    public RenderLivingLabelEvent(final EntityLiving entity, final RenderManager renderManager, final double x, final double y, final double z) {
        this.entity = entity;
        this.renderManager = renderManager;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityLiving getEntity() {
        return entity;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
