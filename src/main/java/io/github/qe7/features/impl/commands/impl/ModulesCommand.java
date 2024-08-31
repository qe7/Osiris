package io.github.qe7.features.impl.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.impl.commands.api.Command;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.utils.local.ChatUtil;

import java.util.stream.Collectors;

public class ModulesCommand extends Command {

    public ModulesCommand() {
        super("Modules", "Displays a list of modules");

        this.setUsage("modules");
    }

    @Override
    public void execute(String[] args) {
        ChatUtil.addPrefixedMessage("Modules", "Modules: " + Osiris.getInstance().getModuleManager().getMap().values().stream().map(Module::getName).collect(Collectors.joining(", ")));
    }
}
