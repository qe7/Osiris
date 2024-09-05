package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
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

    @Subscribe
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = new Listener<>(LivingUpdateEvent.class, event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.gameSettings.keyBindForward.pressed = true;
    });
}
