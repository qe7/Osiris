package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.utils.local.ChatUtil;

import java.util.stream.Collectors;

public class ModulesCommand extends Command {

    public ModulesCommand() {
        super("Modules", "Displays a list of modules");
    }

    @Override
    public void execute(String[] args) {
        ChatUtil.addPrefixedMessage("Modules", "Modules: " + Osiris.getInstance().getModuleManager().getMap().values().stream().map(Module::getName).collect(Collectors.joining(", ")));
    }
}
