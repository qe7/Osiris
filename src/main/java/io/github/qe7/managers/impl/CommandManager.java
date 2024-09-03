package io.github.qe7.managers.impl;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.impl.commands.api.Command;
import io.github.qe7.features.impl.commands.impl.*;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.local.ChatUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import me.zero.alpine.listener.Subscriber;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;

import java.util.ArrayList;
import java.util.List;

public final class CommandManager extends Manager<Class<? extends Command>, Command> implements Subscriber {

    public void initialise() {
        List<Command> commands = new ArrayList<>();

        commands.add(new BindCommand());
        commands.add(new CommandsCommand());
        commands.add(new EnemyCommand());
        commands.add(new FriendCommand());
        commands.add(new HelpCommand());
        commands.add(new ModulesCommand());
        commands.add(new ToggleCommand());

        // Register commands
        commands.forEach(command -> register(command.getClass()));

        // Add modules to map, as they are commands
        Osiris.getInstance().getModuleManager().getMap().values().forEach(module -> getMap().putIfAbsent(module.getClass(), module));

        Osiris.getInstance().getEventBus().subscribe(this);
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

    @Subscribe
    public final Listener<OutgoingPacketEvent> outgoingPacketListener = new Listener<>(event -> {
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

            if (!command.getName().replace(" ", "").equalsIgnoreCase(args[0])) continue;

            command.execute(args);
            return;
        }

        ChatUtil.addPrefixedMessage("Command Manager", "Unknown command: " + args[0]);
    });
}
