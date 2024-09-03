package io.github.qe7.features.impl.modules.impl.chat;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.utils.local.ChatUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.src.Packet3Chat;

import java.util.Objects;

public class MuteDMsModule extends Module {

	private final BooleanSetting onlyEnemies = new BooleanSetting("Only Enemies", true);

    public MuteDMsModule() {
        super("Mute DMs", "Doesn't show /msg messages from players", ModuleCategory.CHAT);
    }

    @Subscribe
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = new Listener<>(event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            if(ChatUtil.getUsernameAndMessage(packet.message) == null)
            	return;

            String username = Objects.requireNonNull(ChatUtil.getUsernameAndMessage(packet.message))[1];

            if((packet.message.startsWith("§7") || packet.message.startsWith("§d")) && !onlyEnemies.getValue()) {
            	packet.message = "";
            } else if((packet.message.startsWith("§7") || packet.message.startsWith("§d")) 
            		&& Osiris.getInstance().getRelationManager().isEnemy(username)
            	    && onlyEnemies.getValue()) {
            	packet.message = "";
            }
        }
    });
}
