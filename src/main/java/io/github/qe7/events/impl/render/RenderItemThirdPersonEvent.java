package io.github.qe7.events.impl.render;

import io.github.qe7.events.api.types.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.src.EnumAction;

@Setter
@Getter
public class RenderItemThirdPersonEvent extends Event {

    private int useItemCount, heldItemRight;

    public RenderItemThirdPersonEvent(final int useItemCount, final int heldItemRight) {
        this.useItemCount = useItemCount;
        this.heldItemRight = heldItemRight;
    }
}
