package io.github.qe7.managers.impl;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.commands.impl.BindCommand;
import io.github.qe7.features.commands.impl.FriendCommand;
import io.github.qe7.features.commands.impl.ToggleCommand;
import io.github.qe7.managers.api.TypeManager;
import io.github.qe7.utils.local.ChatUtility;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;

public final class CommandManager extends TypeManager<Command> {

    private static final Class<Command>[] COMMANDS = new Class[]{
            BindCommand.class,
            FriendCommand.class,
            ToggleCommand.class,
    };

    public void initialise() {
        System.out.println("Initialising CommandManager!");

        for (Class<Command> clazz : COMMANDS) {
            try {
                register(clazz);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize command: " + clazz.getSimpleName() + " - " + e.getMessage());
            }
        }

        Osiris.getInstance().getEventBus().register(this);
        System.out.println("CommandManager initialised!");
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketListener = event -> {
        final Packet packet = event.getPacket();

        if (packet instanceof Packet3Chat) {
            final Packet3Chat chat = (Packet3Chat) packet;

            if (!chat.message.startsWith(".")) return;

            event.setCancelled(true);

            final String[] args = chat.message.substring(1).split(" ");

            for (Command command : getMap().values()) {
                if (command.getName().equalsIgnoreCase(args[0])) {
                    command.execute(args);
                    return;
                }

                for (String alias : command.getAliases()) {
                    if (alias.equalsIgnoreCase(args[0])) {
                        command.execute(args);
                        return;
                    }
                }
            }

            ChatUtility.sendPrefixedMessage("!", "Unknown command: " + args[0]);
        }
    };
}
