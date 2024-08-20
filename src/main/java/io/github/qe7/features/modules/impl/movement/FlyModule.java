package io.github.qe7.features.modules.impl.movement;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.MovementUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FlyModule extends Module {

    public FlyModule() {
        super("Fly", "Want to be superman?", ModuleCategory.MOVEMENT);
    }

    @EventLink
    public final Listener<MotionEvent> motionEventListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.thePlayer.motionY = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            mc.thePlayer.motionY += 0.5;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.thePlayer.motionY -= 0.5;
        }

        mc.thePlayer.onGround = true;
        event.setOnGround(true);

        if (MovementUtil.isMoving()) {
            MovementUtil.setSpeed(0.12);
        } else {
            MovementUtil.setSpeed(0.0);
        }
    };
}
