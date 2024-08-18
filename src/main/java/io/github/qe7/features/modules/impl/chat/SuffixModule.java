package io.github.qe7.features.modules.impl.chat;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.src.Packet3Chat;

public class SuffixModule extends Module {

    public SuffixModule() {
        super("Suffix", "String appends a cringe ass client suffix lol", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            String message = packet.message;

            if (message.startsWith(".") || message.startsWith("/") || message.startsWith(">") || message.startsWith("Welcome") || message.startsWith("Goodbye") || message.startsWith("My friend")) {
                return;
            }

            packet.message = packet.message + " ᴏꜱɪʀɪꜱ";
        }
    };
}
