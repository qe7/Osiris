package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;

public class YawModule extends Module {

    public YawModule() {
        super("Yaw", "Locks your Yaw to a 45* cardinal", ModuleCategory.MISC);
    }

    @Subscribe
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = new Listener<>(LivingUpdateEvent.class, event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.thePlayer.rotationYaw = (Math.round(mc.thePlayer.rotationYaw / 45) * 45);
    });
}
