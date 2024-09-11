package io.github.qe7.events.impl.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public final class RenderItemThirdPersonEvent {

    private int useItemCount, heldItemRight;
}
