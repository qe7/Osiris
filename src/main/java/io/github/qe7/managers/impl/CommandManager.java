package io.github.qe7.managers.impl;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.commands.impl.*;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.local.ChatUtil;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;

public final class CommandManager extends Manager<Class<? extends Command>, Command> {

    private static final Class<Command>[] COMMANDS = new Class[]{
            BindCommand.class,
            CommandsCommand.class,
            EnemyCommand.class,
            FriendCommand.class,
            HelpCommand.class,
            ModulesCommand.class,
            ToggleCommand.class,
    };

    public void initialise() {
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

    public void register(Class<? extends Command> type) {
        try {
            Command command = type.newInstance();
            getMap().putIfAbsent(type, command);
            System.out.println("Registered command: " + type.getSimpleName());
        } catch (Exception e) {
            System.out.println("Failed to register command: " + type.getSimpleName() + " - " + e.getMessage());
        }
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketListener = event -> {
        final Packet packet = event.getPacket();

        if (!(packet instanceof Packet3Chat)) return;

        final Packet3Chat chat = (Packet3Chat) packet;

        if (!chat.message.startsWith(".")) return;

        event.setCancelled(true);

        final String[] args = chat.message.substring(1).split(" ");

        for (Command command : getMap().values()) {
            for (String alias : command.getAliases()) {
                if (!alias.equalsIgnoreCase(args[0])) continue;

                command.execute(args);
                return;
            }

            if (!command.getName().equalsIgnoreCase(args[0])) continue;

            command.execute(args);
            return;
        }

        ChatUtil.addPrefixedMessage("Command Manager", "Unknown command: " + args[0]);
    };
}
