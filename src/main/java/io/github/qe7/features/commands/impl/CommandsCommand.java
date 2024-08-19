package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.utils.local.ChatUtil;

import java.util.stream.Collectors;

public class CommandsCommand extends Command {

    public CommandsCommand() {
        super("Commands", "Displays a list of commands");
    }

    @Override
    public void execute(String[] args) {
        ChatUtil.addPrefixedMessage("Commands", "Commands: " + Osiris.getInstance().getCommandManager().getMap().values().stream().map(Command::getName).collect(Collectors.joining(", ")));
    }
}
