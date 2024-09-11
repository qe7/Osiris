package io.github.qe7.events.impl.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.src.EnumAction;

@Setter
@Getter
@AllArgsConstructor
public final class RenderItemEvent {

    private EnumAction action;

    private float swingProgress;

    private int useItemCount;
}
