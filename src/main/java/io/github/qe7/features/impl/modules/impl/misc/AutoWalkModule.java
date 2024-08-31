package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class AutoWalkModule extends Module {

    public AutoWalkModule() {
        super("Auto Walk", "Sets W to true! W", ModuleCategory.MISC);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.gameSettings.keyBindForward.pressed = true;
    };
}
