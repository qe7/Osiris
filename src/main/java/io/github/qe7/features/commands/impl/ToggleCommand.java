package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.utils.local.ChatUtil;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", "Toggles a given module");

        this.getAliases().add("T");
        this.setUsage("toggle <module>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            ChatUtil.addPrefixedMessage("Toggle", "Invalid arguments");
            ChatUtil.addPrefixedMessage("Toggle", "Usage: " + this.getUsage());
            return;
        }

        String moduleName = args[1];

        if (moduleName.isEmpty()) {
            ChatUtil.addPrefixedMessage("Toggle", "Usage: " + this.getUsage());
            return;
        }

        for (Module module : Osiris.getInstance().getModuleManager().getMap().values()) {
            if (module.getName().replace(" ", "").equalsIgnoreCase(moduleName)) {
                module.setEnabled(!module.isEnabled());
                ChatUtil.addPrefixedMessage("Toggle", "Toggled " + module.getName());
                return;
            }
        }

        ChatUtil.addPrefixedMessage("Toggle", "Module not found");
    }
}
