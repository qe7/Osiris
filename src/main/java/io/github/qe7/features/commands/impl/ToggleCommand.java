package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.utils.local.ChatUtility;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", "Toggles a given module");

        this.setAliases(new String[]{"t"});
        this.setUsage("toggle <module>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            ChatUtility.addPrefixedMessage("Toggle", "Invalid arguments");
            ChatUtility.addPrefixedMessage("Toggle", "Usage: " + this.getUsage());
            return;
        }

        String moduleName = args[1];

        if (moduleName.isEmpty()) {
            ChatUtility.addPrefixedMessage("Toggle", "Usage: " + this.getUsage());
            return;
        }

        for (Module module : Osiris.getInstance().getModuleManager().getMap().values()) {
            if (module.getName().replace(" ", "").equalsIgnoreCase(moduleName)) {
                module.setEnabled(!module.isEnabled());
                ChatUtility.addPrefixedMessage("Toggle", "Toggled " + module.getName());
                return;
            }
        }

        ChatUtility.addPrefixedMessage("Toggle", "Module not found");
    }
}
