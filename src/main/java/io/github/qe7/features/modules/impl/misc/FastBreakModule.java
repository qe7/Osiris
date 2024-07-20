package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.PlayerControllerMP;

public class FastBreakModule extends Module {

    public FastBreakModule() {
        super("Fast Break", "Mines blocks faster", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if (mc.thePlayer.capabilities.isCreativeMode) {
            return;
        }

        if (mc.objectMouseOver == null) {
            return;
        }

        ((PlayerControllerMP) Minecraft.getMinecraft().playerController).curBlockDamageMP *= 2.0F;
    };
}
