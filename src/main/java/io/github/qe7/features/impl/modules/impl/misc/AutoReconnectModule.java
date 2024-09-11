package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.IntSetting;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet255KickDisconnect;

public class AutoReconnectModule extends Module {
	private final IntSetting seconds = new IntSetting("Seconds", 3, 1, 10, 1);

    public AutoReconnectModule() {
        super("Auto Reconnect", "Automatically reconnects to server if player got kicked", ModuleCategory.MISC);
    }
    
    public int getSeconds() {
    	return this.seconds.getValue();
    }

    @Subscribe
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = new Listener<>(IncomingPacketEvent.class, event -> {
    	if(event.getPacket() instanceof Packet255KickDisconnect) {
    		final Minecraft mc = Minecraft.getMinecraft();
    		long disconnectTime = System.currentTimeMillis();
    		while(System.currentTimeMillis() - disconnectTime < (getSeconds() * 1000)) {}
    		if(this.isEnabled())
    			mc.needToReconnect = true;
    	}
    });
}