package io.github.qe7.features.impl.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.impl.commands.api.Command;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.utils.local.ChatUtil;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("Help", "Displays a list of commands and their descriptions");

        this.setUsage("help");
    }

    @Override
    public void execute(String[] args) {
        ChatUtil.addPrefixedMessage("Help", "Commands:");

        for (Command command : Osiris.getInstance().getCommandManager().getMap().values()) {
            if (command instanceof Module) {
                continue;
            }

            ChatUtil.addMessage("§7§l" + command.getName() + "§r - " + command.getDescription());
            ChatUtil.addMessage("Usage: " + command.getUsage());
        }
    }
}
