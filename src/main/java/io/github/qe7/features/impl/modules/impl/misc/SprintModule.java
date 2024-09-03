package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;

public class SprintModule extends Module {

    public static final BooleanSetting omniDirectional = new BooleanSetting("Omni-Directional", true);
    private final BooleanSetting foodCheck = new BooleanSetting("Food Check", true);

    public SprintModule() {
        super("Sprint", "Automatically sprints for the player", ModuleCategory.MISC);
    }

    @Subscribe
    public final Listener<LivingUpdateEvent> listener = new Listener<>(event -> {
        final Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayerSP player = mc.thePlayer;

        if (player == null) return;

        Minecraft.getMinecraft().thePlayer.setSprinting(shouldSprint());
    });

    private boolean shouldSprint() {
        final Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayerSP player = mc.thePlayer;

        if (player == null) return false;
        if (player.isCollidedHorizontally) return false;
        if (player.isSneaking()) return false;
        if (foodCheck.getValue() && player.getFoodStats().getFoodLevel() <= 6) return false;

        if (omniDirectional.getValue()) {
            return mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0;
        } else {
            return mc.thePlayer.movementInput.moveForward > 0;
        }
    }
}
