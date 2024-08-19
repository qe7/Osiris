package io.github.qe7.features.modules.impl.chat;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.accounts.Account;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet3Chat;

public class AutoLoginModule extends Module {

    public AutoLoginModule() {
        super("Auto Login", "Automatically logins in, FOR LAZY PEOPLE", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEvent = event -> {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        if (event.getPacket() instanceof Packet3Chat) {
            Packet3Chat chat = (Packet3Chat) event.getPacket();

            final String normalizedMessage = chat.message.replaceAll("§.", "").toLowerCase();

            if (normalizedMessage.contains("please login with")) {
                final String normalizedName = Minecraft.getMinecraft().session.username;

                Account account = Osiris.getInstance().getAccountManager().getAccount(normalizedName);

                if (account != null) {
                    ChatUtil.sendMessage("/login " + account.getPassword());
                    ChatUtil.addPrefixedMessage("Auto Login", "Logged in as " + normalizedName);
                } else {
                    ChatUtil.addPrefixedMessage("Auto Login", "No account found for " + normalizedName);
                }
            }
        }
    };
}
