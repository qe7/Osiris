package io.github.qe7.features.impl.modules.impl.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.utils.local.ChatUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.src.Packet3Chat;

public class BetterChatModule extends Module {

	private final BooleanSetting time = new BooleanSetting("Show time", true);

    public BetterChatModule() {
        super("Better Chat", "Makes chat look better", ModuleCategory.CHAT);
    }

    @Subscribe
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = new Listener<>(IncomingPacketEvent.class, event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();
            StringBuilder output = new StringBuilder();
            String[] temp = ChatUtil.getUsernameAndMessage(packet.message);
            if(temp == null)
            	return;
            if(this.time.getValue()) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");	//24-h format. No 12-h format.
            	output.append("\u00A77[" + format.format(new Date(System.currentTimeMillis())) + "]\u00A7r ");   
            }
            if(ChatUtil.isDMMessage(packet.message) == 1) {
            	if(Osiris.getInstance().getRelationManager().isFriend(temp[1])) {
                	output.append("\u00A7a");
            	} else if(Osiris.getInstance().getRelationManager().isEnemy(temp[1])) {
                	output.append("\u00A7c");
            	}
            	output.append(temp[1]);
            	output.append(" \u00A7dtells\u00A7r:\u00A77");
            	output.append(temp[0]);
            	packet.message = output.toString();
            	return;
            } else if(ChatUtil.isDMMessage(packet.message) == 2) {
            	output.append("\u00A7dYou tell ");
            	if(Osiris.getInstance().getRelationManager().isFriend(temp[1])) {
                	output.append("\u00A7a");
            	} else if(Osiris.getInstance().getRelationManager().isEnemy(temp[1])) {
                	output.append("\u00A7c");
            	} else {
            		output.append("\u00A7r");
            	}
            	output.append(temp[1]);
            	output.append("\u00A7r:\u00A77");
            	output.append(temp[0]);
            	packet.message = output.toString();
            	return;
            } else {
            	if(Osiris.getInstance().getRelationManager().isFriend(temp[1])) {
            		output.append("\u00A78[\u00A7a" + temp[1] + "\u00A78]");
            	} else if(Osiris.getInstance().getRelationManager().isEnemy(temp[1])) {
            		output.append("\u00A78[\u00A7c" + temp[1] + "\u00A78]");
            	} else
            		output.append("\u00A78[\u00A77" + temp[1] + "\u00A78]");
            	output.append("\u00A7r: ");
            	output.append(temp[0]);
            	packet.message = output.toString();
            	return;
            }
        }
    });
}
