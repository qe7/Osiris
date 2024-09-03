package io.github.qe7.events.impl.render;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RenderItemThirdPersonEvent {

    private int useItemCount, heldItemRight;

    public RenderItemThirdPersonEvent(final int useItemCount, final int heldItemRight) {
        this.useItemCount = useItemCount;
        this.heldItemRight = heldItemRight;
    }
}
