package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;

public class AutoWalkModule extends Module {

    public AutoWalkModule() {
        super("Auto Walk", "Sets W to true! W", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.gameSettings.keyBindForward.pressed = true;
    };
}
