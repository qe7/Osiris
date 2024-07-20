package io.github.qe7.events.impl.render;

import io.github.qe7.events.api.types.Event;
import net.minecraft.src.EnumAction;

public class RenderItemEvent extends Event {

    private EnumAction action;

    private float swingProgress;

    private int useItemCount;

    public RenderItemEvent(final EnumAction action, final float swingProgress, final int useItemCount) {
        this.action = action;
        this.swingProgress = swingProgress;
        this.useItemCount = useItemCount;
    }

    public EnumAction getAction() {
        return action;
    }

    public void setAction(EnumAction action) {
        this.action = action;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public void setSwingProgress(float swingProgress) {
        this.swingProgress = swingProgress;
    }

    public int getUseItemCount() {
        return useItemCount;
    }

    public void setUseItemCount(int useItemCount) {
        this.useItemCount = useItemCount;
    }
}
