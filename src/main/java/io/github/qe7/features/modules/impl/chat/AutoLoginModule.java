package io.github.qe7.features.modules.impl.chat;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.accounts.Account;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.ChatUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet3Chat;

public class AutoLoginModule extends Module {

    public AutoLoginModule() {
        super("Auto Login", "Automatically logins in, FOR LAZY PEOPLE", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEvent = event -> {
        try {
            if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
                return;
            }

            if (event.getPacket() instanceof Packet3Chat) {
                Packet3Chat chat = (Packet3Chat) event.getPacket();

                final String normalizedMessage = chat.message.replaceAll("§.", "").toLowerCase();

                if (normalizedMessage.contains("please login with")) {
                    final String normalizedName = Minecraft.getMinecraft().thePlayer.username;

                    Account account = Osiris.getInstance().getAccountManager().getAccount(normalizedName);

                    if (account != null) {
                        ChatUtility.sendMessage("/login " + account.getPassword());
                        ChatUtility.addPrefixedMessage("Auto Login", "Logged in as " + normalizedName);
                    } else {
                        ChatUtility.addPrefixedMessage("Auto Login", "No account found for " + normalizedName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}
