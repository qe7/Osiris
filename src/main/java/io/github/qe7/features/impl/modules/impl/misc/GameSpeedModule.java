package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.DoubleSetting;
import net.minecraft.client.Minecraft;

public class GameSpeedModule extends Module {

    private final DoubleSetting speed = new DoubleSetting("Speed", 1.0, 0.1, 10.0, 0.1);

    public GameSpeedModule() {
        super("Game Speed", "Modifies the games speed/Timer", ModuleCategory.MISC);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Minecraft.timer.timerSpeed = 1.0f;
    }

    @EventLink
    public final Listener<MotionEvent> motionListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) {
            return;
        }

        Minecraft.timer.timerSpeed = speed.getValue().floatValue();
    };
}
