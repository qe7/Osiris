package io.github.qe7.features.impl.modules.impl.chat;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.accounts.Account;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.ChatUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet3Chat;

public class AutoLoginModule extends Module {

    public AutoLoginModule() {
        super("Auto Login", "Automatically logins in, FOR LAZY PEOPLE", ModuleCategory.CHAT);
    }

    @Subscribe
    public final Listener<IncomingPacketEvent> incomingPacketEvent = new Listener<>(IncomingPacketEvent.class, event -> {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        if (event.getPacket() instanceof Packet3Chat) {
            Packet3Chat chat = (Packet3Chat) event.getPacket();

            if (chat.message.startsWith("§c") && chat.message.contains("Please login with")) {
                final String normalizedName = Minecraft.getMinecraft().session.username;

                Account account = Osiris.getInstance().getAccountManager().getMap().get(normalizedName);

                if (account != null) {
                    ChatUtil.sendMessage("/login " + account.getPassword());
                    ChatUtil.addPrefixedMessage("Auto Login", "Logged in as " + normalizedName);
                } else {
                    ChatUtil.addPrefixedMessage("Auto Login", "No account found for " + normalizedName);
                }
            }
        }
    });
}