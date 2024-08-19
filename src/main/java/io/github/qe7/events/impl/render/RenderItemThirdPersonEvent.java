package io.github.qe7.events.impl.render;

import io.github.qe7.events.api.types.Event;
import net.minecraft.src.EnumAction;

public class RenderItemThirdPersonEvent extends Event {

    private int useItemCount, heldItemRight;

    public RenderItemThirdPersonEvent(final int useItemCount, final int heldItemRight) {
        this.useItemCount = useItemCount;
        this.heldItemRight = heldItemRight;
    }

    public int getUseItemCount() {
        return useItemCount;
    }

    public void setUseItemCount(int useItemCount) {
        this.useItemCount = useItemCount;
    }

    public int getHeldItemRight() {
        return heldItemRight;
    }

    public void setHeldItemRight(int heldItemRight) {
        this.heldItemRight = heldItemRight;
    }
}
