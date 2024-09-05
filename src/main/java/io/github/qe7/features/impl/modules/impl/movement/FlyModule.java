package io.github.qe7.features.impl.modules.impl.movement;

import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.MovementUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FlyModule extends Module {

    public FlyModule() {
        super("Fly", "Want to be superman?", ModuleCategory.MOVEMENT);
    }

    @Subscribe
    public final Listener<MotionEvent> motionEventListener = new Listener<>(MotionEvent.class, event -> {
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
    });
}
