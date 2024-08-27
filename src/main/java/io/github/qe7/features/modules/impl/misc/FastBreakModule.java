package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.api.settings.impl.DoubleSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.PlayerControllerSP;
import net.minecraft.src.Vec3D;

public class FastBreakModule extends Module {

    private final DoubleSetting minDamage = new DoubleSetting("Start damage", 0.5, 0.1, 1.0, 0.1);

    public FastBreakModule() {
        super("Fast Break", "Mines blocks faster", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.capabilities.isCreativeMode) {
            return;
        }

        if (mc.objectMouseOver == null) {
            return;
        }

        mc.leftClickCounter = 0;

        if(mc.playerController instanceof PlayerControllerMP) {
            PlayerControllerMP playerController = (PlayerControllerMP) mc.playerController;

            if (playerController.curBlockDamageMP == 0.0F) {
                return;
            }

            if (playerController.curBlockDamageMP < minDamage.getValue()) {
                playerController.curBlockDamageMP = minDamage.getValue().floatValue();
            }
        } else if(mc.playerController instanceof PlayerControllerSP) {
            PlayerControllerSP playerController = (PlayerControllerSP) mc.playerController;

            if (playerController.curBlockDamage == 0.0F) {
                return;
            }

            if (playerController.curBlockDamage < minDamage.getValue()) {
                playerController.curBlockDamage = minDamage.getValue().floatValue();
            }
        }
    };
}
