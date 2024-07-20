package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.utils.math.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet3Chat;

public class ChatUtilsModule extends Module {

    private final BooleanSetting greenText = new BooleanSetting("Green Text", true);
    private final BooleanSetting suffix = new BooleanSetting("Suffix", true);
    private final BooleanSetting welcomer = new BooleanSetting("Welcomer", true);

    private final Stopwatch welcomerStopwatch = new Stopwatch();

    public ChatUtilsModule() {
        super("Chat Utils", ">", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            String message = packet.message;

            if (message.startsWith(".") || message.startsWith("/") || message.startsWith(">") || message.startsWith("Welcome") || message.startsWith("Goodbye") || message.startsWith("My friend")) {
                return;
            }

            if (greenText.getValue()) {
                packet.message = "> " + message;
            }

            if (suffix.getValue()) {
                packet.message = packet.message + " \u1D0F\uA731\u026A\u0280\u026A\uA731";
            }

            this.welcomerStopwatch.reset(); // Reset the stopwatch when sending a message (to prevent spam kick)
        }
    };

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();

            final String message = packet.message;

            if (welcomer.getValue()) {
                if (Minecraft.getMinecraft().thePlayer == null) return;
                if (this.welcomerStopwatch.elapsed(1000)) {
                    if (message.contains("joined the game.")) {
                        String cleanName = message.split(" ")[0].replaceAll("\u00A7.", "");

                        if (Osiris.getInstance().getFriendManager().isFriend(cleanName)) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("My friend, " + cleanName + ", joined!");
                        } else {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("Welcome, " + cleanName + "!");
                        }
                        this.welcomerStopwatch.reset();
                    } else if (message.contains("left the game.")) {
                        String cleanName = message.split(" ")[0].replaceAll("\u00A7.", "");

                        if (Osiris.getInstance().getFriendManager().isFriend(cleanName)) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("My friend, " + cleanName + ", left!");
                        } else {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("Goodbye, " + cleanName + "!");
                        }
                        this.welcomerStopwatch.reset();
                    }
                }
            }
        }
    };
}
