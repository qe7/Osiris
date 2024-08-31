package io.github.qe7.features.impl.modules.impl.chat;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import net.minecraft.src.Packet3Chat;

public class HideChatModule extends Module {
	private final BooleanSetting disableWelcomer = new BooleanSetting("Disable welcomer", true);
    public HideChatModule() {
        super("Hide Chat", "Hides chat and you can't type in it", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            String message = packet.message;

            if (message.startsWith(".") || !disableWelcomer.getValue() && (message.startsWith("Welcome") || message.startsWith("Goodbye") || message.startsWith("My friend"))) {
                return;
            }

            packet.message = "";
        }
    };
    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();
            packet.message = "";
        }
    };
}
