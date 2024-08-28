package io.github.qe7.utils.local;

import io.github.qe7.utils.UtilBase;
import net.minecraft.client.Minecraft;

public final class ChatUtil extends UtilBase {

    /**
     * Sends a message to the chat.
     *
     * @param message the message to send
     */
    public static void addMessage(final String message) {
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
    public static void addPrefixedMessage(final String prefix, final String message) {
        addMessage("§7§l(" + prefix + ") §r§f" + message);
    }

    /**
     * Sends a message to the chat.
     *
     * @param message the message to send
     */
    public static void sendMessage(final String message) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;

        mc.thePlayer.sendChatMessage(message);
    }
    
    /**
     * Separates username and message from chat message
     *
     * @param message to separate
     */
    public static String[] getUsernameAndMessage(String message) {
    	if((message.startsWith("§7") || message.startsWith("§d"))) {
	    	boolean isTell = false;
			if(message.startsWith("§7"))
				isTell = true;
			try {
				if(isTell) {
					if(message.split(" whispers ").length <= 1) {
						return null;
					}
					String playerNickname = (message.split(" "))[0].substring(2);
					String text = message.split(" whispers ")[1]; 
					String[] out = {text, playerNickname};
					return out;
				} else {
					String playerNickname = (message.split(" "))[1].substring(0, (message.split(" "))[1].indexOf(":"));
					String text = message.substring(8 + playerNickname.length());
					String[] out = {text, playerNickname};
					return out;
				}
			} catch(Exception ex) {
				return null;
			}
    	} else if(message.startsWith("<"))  {
    		if(message.indexOf(" ") == 0) 
    			return null;
    		String text = message.substring(message.indexOf(" "));
    		String playerNickname;
    		if(message.split(" ")[0].substring(1,2).equals("§")){
    			playerNickname = (message.split(" "))[0].substring(3, message.split(" ")[0].lastIndexOf("§"));
    		} else 
    			playerNickname = (message.split(" "))[0].substring(1, message.indexOf(">"));
    		String[] out = {text, playerNickname};
    		return out;
    	} else {
    		return null;
    	}
    }
}
