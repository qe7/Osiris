package io.github.qe7.features.modules.impl.chat;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.math.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet3Chat;

public class WelcomerModule extends Module {

    private final Stopwatch welcomerStopwatch = new Stopwatch();

    public WelcomerModule() {
        super("Welcomer", "Sends a welcome and goodbye message automatically", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            this.welcomerStopwatch.reset();
        }
    };

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            final String message = packet.message;

            if (Minecraft.getMinecraft().thePlayer == null) return;
            if (this.welcomerStopwatch.elapsed(5000)) {
                if (message.startsWith("§e") && message.contains("joined the game.")) {
                    String cleanName = message.split(" ")[0].replaceAll("§.", "");

                    if (cleanName.equals(Minecraft.getMinecraft().thePlayer.username)) {
                        return;
                    }

                    if (Osiris.getInstance().getRelationManager().isFriend(cleanName)) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("My friend, " + cleanName + ", joined!");
                    } else {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("Welcome, " + cleanName + "!");
                    }
                } else if (message.startsWith("§e") && message.contains("left the game.")) {
                    String cleanName = message.split(" ")[0].replaceAll("§.", "");

                    if (cleanName.equals(Minecraft.getMinecraft().thePlayer.username)) {
                        return;
                    }

                    if (Osiris.getInstance().getRelationManager().isFriend(cleanName)) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("My friend, " + cleanName + ", left!");
                    } else {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("Goodbye, " + cleanName + "!");
                    }
                }
            }
        }
    };
}
