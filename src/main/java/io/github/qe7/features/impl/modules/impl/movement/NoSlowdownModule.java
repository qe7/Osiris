package io.github.qe7.features.impl.modules.impl.movement;

import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.events.impl.player.PostMotionEvent;
import io.github.qe7.events.impl.player.SlowdownEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.PacketUtil;
import me.zero.alpine.event.Cancellable;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;

public class NoSlowdownModule extends Module {

    public NoSlowdownModule() {
        super("No Slowdown", "Disables slowdown for local player", ModuleCategory.MOVEMENT);
    }

    @Subscribe
    public final Listener<SlowdownEvent> slowdownListener = new Listener<>(Cancellable::cancel);

    @Subscribe
    public final Listener<MotionEvent> motionListener = new Listener<>(event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.isBlocking()) {
            PacketUtil.sendPacket(new Packet15Place());
        }
    });

    @Subscribe
    public final Listener<PostMotionEvent> postMotionListener = new Listener<>(event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.isBlocking()) {
            PacketUtil.sendPacket(new Packet14BlockDig(5, 0, 0, 0, 0));
        }
    });
}
