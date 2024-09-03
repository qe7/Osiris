package io.github.qe7.features.impl.modules.impl.chat;

import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.src.Packet3Chat;

public class GreenTextModule extends Module {

    public GreenTextModule() {
        super("Green Text", "Automatically green texts for you", ModuleCategory.CHAT);
    }

    @Subscribe
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener =  new Listener<>(event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            String message = packet.message;

            if (message.startsWith(".") || message.startsWith("/") || message.startsWith(">") || message.startsWith("Welcome") || message.startsWith("Goodbye") || message.startsWith("My friend")) {
                return;
            }

            // check if length would be over 100 characters
            if (packet.message.length() + 2 > 100) {
                return;
            }

            packet.message = "> " + message;
        }
    });
}
