package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.Osiris;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.accounts.Account;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.configs.FileUtil;
import io.github.qe7.utils.local.ChatUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import me.zero.alpine.listener.Subscriber;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;

import java.util.Objects;

public final class AccountManager extends Manager<String, Account> implements Subscriber {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private String possiblePassword = "";

    public void initialise() {

        this.loadAccounts();

        Osiris.getInstance().getEventBus().subscribe(this);
        System.out.println("AccountManager initialised!");
    }

    public void register(Account type) {
        this.getMap().put(type.getUsername(), type);
    }

    public void saveAccounts() {
        JsonObject object = new JsonObject();

        this.getMap().forEach((username, account) -> object.add(username, GSON.toJsonTree(account)));

        FileUtil.writeFile("accounts", GSON.toJson(object));
    }

    public void loadAccounts() {
        String config = FileUtil.readFile("accounts");

        if (config == null) {
            return;
        }

        JsonObject object = GSON.fromJson(config, JsonObject.class);

        try {
            object.entrySet().forEach(entry -> {
                final String password = entry.getValue().getAsJsonObject().get("password").getAsString();
                Account account = new Account(entry.getKey(), password);
                this.register(account);
            });
        } catch (Exception e) {
            // Crash reported by user "aqu1noxxfr" on Discord, will try figure out the issue :)
            System.out.println("Failed to load accounts - " + e.getMessage());
        }
    }

    @Subscribe
    public final Listener<IncomingPacketEvent> incomingPacketListener = new Listener<>(event -> {
        final Packet packet = event.getPacket();

        if (packet instanceof Packet3Chat) {
            final Packet3Chat chat = (Packet3Chat) packet;

            final String normalMessage = chat.message.replaceAll("§.", "");

            if (normalMessage.equalsIgnoreCase("Successful login!")) {
                if (this.possiblePassword.isEmpty()) {
                    ChatUtil.addPrefixedMessage("Account Manager", "Failed to find password.");
                    return;
                }
                Account account;
                if (this.getMap().get(Minecraft.getMinecraft().session.username) != null) {
                    account = this.getMap().get(Minecraft.getMinecraft().session.username);
                    if (account == null) return;
                    if (account.getPassword() != null && !account.getPassword().isEmpty() && Objects.equals(account.getPassword(), this.possiblePassword))
                        return;
                    account.setPassword(this.possiblePassword);
                    ChatUtil.addPrefixedMessage("Account Manager", "Updated password for " + account.getUsername());
                } else {
                    account = new Account(Minecraft.getMinecraft().session.username, this.possiblePassword);
                    this.register(account);
                    ChatUtil.addPrefixedMessage("Account Manager", "Added account " + account.getUsername());
                }
                this.saveAccounts();
            } else if (normalMessage.equalsIgnoreCase("Wrong password.")) {
                this.possiblePassword = "";
            }
        }
    });

    @Subscribe
    public final Listener<OutgoingPacketEvent> outgoingPacketListener = new Listener<>(event -> {
        final Packet packet = event.getPacket();

        if (packet instanceof Packet3Chat) {
            final Packet3Chat chat = (Packet3Chat) packet;

            // format "/login password"
            if (chat.message.startsWith("/login") || chat.message.startsWith("/register")) {
                String[] args = chat.message.split(" ");
                if (args.length == 2) {
                    this.possiblePassword = args[1];
                }
            }
        }
    });
}
