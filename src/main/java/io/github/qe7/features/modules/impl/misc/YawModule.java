package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;

public class YawModule extends Module {

    public YawModule() {
        super("Yaw", "Locks your Yaw to a 45* cardinal", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.thePlayer.rotationYaw = (Math.round(mc.thePlayer.rotationYaw / 45) * 45);
    };
}
