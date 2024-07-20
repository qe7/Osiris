package io.github.qe7.utils.local;

import io.github.qe7.utils.UtilityBase;
import net.minecraft.client.Minecraft;

public final class ChatUtility extends UtilityBase {
    /**
     * Sends a message to the chat.
     *
     * @param message the message to send
     */
    public static void sendMessage(final String message) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.thePlayer.addChatMessage(message);
    }

    /**
     * Sends a prefixed message to the chat.
     *
     * @param prefix the prefix to use
     * @param message the message to send
     */
    public static void sendPrefixedMessage(final String prefix, final String message) {
        sendMessage("\u00A77\u00A7l(" + prefix + ") \u00A7r\u00A7f" + message);
    }
}
