package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.IntSetting;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet4UpdateTime;

public class WorldTimeModule extends Module {

    private final IntSetting time = new IntSetting("Time", 0, 0, 24000, 1000);

    public WorldTimeModule() {
        super("World Time", "Sets the time of the world!", ModuleCategory.RENDER);
    }

    @Subscribe
    public final Listener<RenderScreenEvent> renderScreenEvent = new Listener<>(RenderScreenEvent.class, event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) {
            return;
        }

        mc.theWorld.setWorldTime(time.getValue());
    });

    @Subscribe
    public final Listener<IncomingPacketEvent> incomingPacketEvent = new Listener<>(IncomingPacketEvent.class, event -> {
        if (event.getPacket() instanceof Packet4UpdateTime) {
            event.setCancelled(true);
        }
    });
}
