package io.github.qe7.features.impl.modules.impl.combat;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.PacketUtil;
import io.github.qe7.utils.math.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet7UseEntity;

public class CriticalsModule extends Module {

    private final Stopwatch stopwatch = new Stopwatch();

    public CriticalsModule() {
        super("Criticals", "Automatically crits on hit", ModuleCategory.COMBAT);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        if (event.getPacket() instanceof Packet7UseEntity) {
            final Packet7UseEntity useEntity = (Packet7UseEntity) event.getPacket();
            if (useEntity.isLeftClick != 1) return;
            if (mc.thePlayer.onGround && stopwatch.elapsed(200)) {
                PacketUtil.sendPacket(new Packet11PlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY + 0.5F, mc.thePlayer.posY + 0.5F, mc.thePlayer.posZ, false));
                PacketUtil.sendPacket(new Packet11PlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                stopwatch.reset();
            }
        }
    };
}
