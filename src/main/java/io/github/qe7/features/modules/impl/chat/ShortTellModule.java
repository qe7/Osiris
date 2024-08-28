package io.github.qe7.features.modules.impl.chat;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.src.Packet3Chat;

public class ShortTellModule extends Module {
    public ShortTellModule() {
        super("Short Tell", "With this module you can '/tell' via '@username'", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            String message = packet.message;

            if (!message.startsWith("@")) {
                return;
            } else if(message.split(" ").length < 2) {
            	return;
            }
            String username = message.split(" ")[0].substring(1);
            String messageToSend = message.substring(message.split(" ")[0].length() + 1);
            packet.message = "/tell " + username + " " + messageToSend;
        }
    };
}
