package io.github.qe7.features.impl.modules.impl.movement;

import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.MovementUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class JesusModule extends Module {

    public JesusModule() {
        super("Jesus", "Become Jesus and walk on water.", ModuleCategory.MOVEMENT);
    }

    @Subscribe
    public final Listener<MotionEvent> motionEventListener = new Listener<>(event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;
        if (!MovementUtil.isMoving()) return;

        if (mc.thePlayer.isInWater()) {
            // if sneaking or jumping continue
            if (mc.thePlayer.isSneaking() || Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode)) return;

            mc.thePlayer.motionY = 0.1;
        }
    });
}
