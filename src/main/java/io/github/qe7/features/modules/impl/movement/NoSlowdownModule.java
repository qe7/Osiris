package io.github.qe7.features.modules.impl.movement;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.events.impl.player.PostMotionEvent;
import io.github.qe7.events.impl.player.SlowdownEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;

public class NoSlowdownModule extends Module {

    public NoSlowdownModule() {
        super("No Slowdown", "Disables slowdown for local player", ModuleCategory.MOVEMENT);
    }

    @EventLink
    public final Listener<SlowdownEvent> slowdownListener = event -> event.setCancelled(true);

    @EventLink
    public final Listener<MotionEvent> motionListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.isBlocking()) {
            PacketUtil.sendPacket(new Packet15Place());
        }
    };

    @EventLink
    public final Listener<PostMotionEvent> postMotionListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.isBlocking()) {
            PacketUtil.sendPacket(new Packet14BlockDig(5, 0, 0, 0, 0));
        }
    };
}
