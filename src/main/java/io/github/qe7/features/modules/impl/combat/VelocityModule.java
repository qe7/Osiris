package io.github.qe7.features.modules.impl.combat;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.IntSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet28EntityVelocity;

public class VelocityModule extends Module {

    private final IntSetting horizontal = new IntSetting("Horizontal", 0, -100, 100, 1);
    private final IntSetting vertical = new IntSetting("Vertical", 0, -100, 100, 1);

    public VelocityModule() {
        super("Velocity", "Manipulates velocity given to local player", ModuleCategory.COMBAT);
    }

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketListener = event -> {
        final Packet eventPacket = event.getPacket();

        if (eventPacket instanceof Packet28EntityVelocity) {
            final Packet28EntityVelocity velocity = (Packet28EntityVelocity) eventPacket;

            if (Minecraft.getMinecraft().thePlayer == null) return;
            if (velocity.entityId != Minecraft.getMinecraft().thePlayer.entityId) return;
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.setCancelled(true);

            velocity.motionX = (int) (velocity.motionX * (horizontal.getValue() / 100.0));
            velocity.motionY = (int) (velocity.motionY * (vertical.getValue() / 100.0));
            velocity.motionZ = (int) (velocity.motionZ * (horizontal.getValue() / 100.0));
        }
    };
}
