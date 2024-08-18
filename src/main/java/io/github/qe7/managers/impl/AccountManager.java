package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.accounts.Account;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.managers.api.interfaces.Register;
import io.github.qe7.managers.api.interfaces.Unregister;
import io.github.qe7.utils.configs.FileUtility;
import io.github.qe7.utils.local.ChatUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;

import java.util.Objects;

public final class AccountManager extends Manager<Account, String> implements Register<Account>, Unregister<Account> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private String possiblePassword = "";

    public void initialise() {

        this.loadAccounts();

        Osiris.getInstance().getEventBus().register(this);
        System.out.println("AccountManager initialised!");
    }

    @Override
    public void register(Account type) {
        this.getMap().put(type, type.getUsername());
    }

    @Override
    public void unregister(Account type) {
        this.getMap().remove(type);
    }

    public void saveAccounts() {
        JsonObject object = new JsonObject();

        this.getMap().forEach((account, s) -> object.add(s, account.serialize()));

        FileUtility.writeFile("accounts", GSON.toJson(object));
    }

    public void loadAccounts() {
        String config = FileUtility.readFile("accounts");

        if (config == null) {
            return;
        }

        JsonObject object = GSON.fromJson(config, JsonObject.class);

        object.entrySet().forEach(entry -> {
            final String password = entry.getValue().getAsJsonObject().get("password").getAsString();
            Account account = new Account(entry.getKey(), password);
            this.register(account);
        });
    }

    public Account getAccount(String username) {
        for (Account account : this.getMap().keySet()) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketListener = event -> {
        final Packet packet = event.getPacket();

        if (packet instanceof Packet3Chat) {
            final Packet3Chat chat = (Packet3Chat) packet;

            final String normalMessage = chat.message.replaceAll("§.", "");

            if (normalMessage.equalsIgnoreCase("Successful login!")) {
                if (this.possiblePassword.isEmpty()) {
                    ChatUtility.addPrefixedMessage("Account Manager", "Failed to find password.");
                    return;
                }
                Account account;
                if (this.getAccount(Minecraft.getMinecraft().session.username) != null) {
                    account = this.getAccount(Minecraft.getMinecraft().session.username);
                    if (account == null) return;
                    if (account.getPassword() != null && !account.getPassword().isEmpty() && Objects.equals(account.getPassword(), this.possiblePassword)) return;
                    account.setPassword(this.possiblePassword);
                    ChatUtility.addPrefixedMessage("Account Manager", "Updated password for " + account.getUsername());
                } else {
                    account = new Account(Minecraft.getMinecraft().session.username, this.possiblePassword);
                    this.register(account);
                    ChatUtility.addPrefixedMessage("Account Manager", "Added account " + account.getUsername());
                }
                this.saveAccounts();
            } else if (normalMessage.equalsIgnoreCase("Wrong password.")) {
                this.possiblePassword = "";
            }
        }
    };

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketListener = event -> {
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
    };
}
