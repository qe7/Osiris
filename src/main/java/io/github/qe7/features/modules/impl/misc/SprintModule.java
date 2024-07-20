package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

public class SprintModule extends Module {

    public static final BooleanSetting omniDirectional = new BooleanSetting("Omni-Directional", true);

    public SprintModule() {
        super("Sprint", "Automatically sprints for the player", ModuleCategory.MISC);

        this.setEnabled(true);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> listener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayerSP player = mc.thePlayer;

        if (player == null) return;

        Minecraft.getMinecraft().thePlayer.setSprinting(shouldSprint());
    };

    private boolean shouldSprint() {
        final Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayerSP player = mc.thePlayer;

        if (player == null) return false;
        if (mc.currentScreen != null) return false;
        if (player.isCollidedHorizontally) return false;
        if (player.isSneaking()) return false;

        if (omniDirectional.getValue()) {
            return Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode) || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.keyCode) || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.keyCode) || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.keyCode);
        } else {
            return Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode);
        }
    }
}
