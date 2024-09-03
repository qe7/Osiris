package io.github.qe7.features.impl.modules.impl.movement;

import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
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

    @Subscribe
    public final Listener<RenderScreenEvent> renderScreenListener = new Listener<>(event -> {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }

        Minecraft.getMinecraft().thePlayer.stepHeight = 1.0f;
    });
}
