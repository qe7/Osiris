package io.github.qe7.features.modules.impl.render;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.IntSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet4UpdateTime;

public class WorldTimeModule extends Module {

    private final IntSetting time = new IntSetting("Time", 0, 0, 24000, 1000);

    public WorldTimeModule() {
        super("World Time", "Sets the time of the world!", ModuleCategory.RENDER);
    }

    @EventLink
    public final Listener<RenderScreenEvent> renderScreenEvent = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) {
            return;
        }

        mc.theWorld.setWorldTime(time.getValue());
    };

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEvent = event -> {
        if (event.getPacket() instanceof Packet4UpdateTime) {
            event.setCancelled(true);
        }
    };
}
