package io.github.qe7.utils.local;

import io.github.qe7.utils.UtilityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet;

/**
 * Utility class for packets.
 */
public final class PacketUtility extends UtilityBase {

    /**
     * Sends a packet to the server
     *
     * @param packet the packet to send
     */
    public static void sendPacket(Packet packet) {
        Minecraft.getMinecraft().getSendQueue().addToSendQueue(packet);
    }
}
