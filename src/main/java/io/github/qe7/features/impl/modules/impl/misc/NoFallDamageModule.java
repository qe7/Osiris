package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet10Flying;

public class NoFallDamageModule extends Module {

    public NoFallDamageModule() {
        super("No Fall Damage", "^ Module name", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<MotionEvent> motionEventListener = event -> {
        final Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.thePlayer.fallDistance >= 2.5f) {
            minecraft.thePlayer.fallDistance = 0.0f;
            PacketUtil.sendPacket(new Packet10Flying(true));
        }
    };
}
