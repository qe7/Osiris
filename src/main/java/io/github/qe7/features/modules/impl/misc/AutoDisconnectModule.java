package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.DoubleSetting;
import net.minecraft.client.Minecraft;

public class AutoDisconnectModule extends Module {

    private final DoubleSetting health = new DoubleSetting("Health", 14.0, 0.5, 20.0, 0.5f);

    public AutoDisconnectModule() {
        super("Auto Disconnect", "Automatically disconnects the player if health reaches below a given point", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.getHealth() <= health.getValue()) {
            mc.theWorld.sendQuittingDisconnectingPacket();
        }
    };
}
