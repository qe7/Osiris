package io.github.qe7.features.modules.impl.movement;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;

public class StepModule extends Module {

    private float oldStepHeight;

    public StepModule() {
        super("Step", "Allows the player to step up higher heights", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }

        oldStepHeight = Minecraft.getMinecraft().thePlayer.stepHeight;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        
        Minecraft.getMinecraft().thePlayer.stepHeight = oldStepHeight;
    }

    @EventLink
    public final Listener<RenderScreenEvent> renderScreenListener = event -> Minecraft.getMinecraft().thePlayer.stepHeight = 1.0F;
}
