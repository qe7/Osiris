package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.IntSetting;
import net.minecraft.client.Minecraft;

public class FastPlaceModule extends Module {

    private final IntSetting speed = new IntSetting("Speed", 0, 0, 4, 1);

    public FastPlaceModule() {
        super("Fast Place", "Allows the player to place blocks faster", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.capabilities.isCreativeMode) {
            return;
        }

        if (mc.rightClickDelayTimer > speed.getValue()) {
            mc.rightClickDelayTimer = speed.getValue();
        }
    };
}
