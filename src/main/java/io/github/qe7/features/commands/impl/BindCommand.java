package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.utils.local.ChatUtility;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "Test command");

        this.setAliases(new String[]{"b"});
        this.setUsage("bind <module> <key>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            ChatUtility.sendPrefixedMessage("!", "Invalid arguments");
            return;
        }

        final String moduleName = args[1];
        final String key = args[2];

        for (Module module : Osiris.getInstance().getModuleManager().getMap().values()) {
            if (module.getName().replace(" ", "").equalsIgnoreCase(moduleName)) {
                if (key.equalsIgnoreCase("none")) {
                    module.setKeyCode(-1);
                    ChatUtility.sendPrefixedMessage("!", "Unbound " + module.getName());
                    return;
                }

                int keyCode;

                try {
                    keyCode = Keyboard.getKeyIndex(key.toUpperCase());
                } catch (NumberFormatException e) {
                    ChatUtility.sendPrefixedMessage("!", "Invalid key");
                    return;
                }

                module.setKeyCode(keyCode);
                ChatUtility.sendPrefixedMessage("!", "Bound " + module.getName() + " to " + key);
                return;
            }
        }

        ChatUtility.sendPrefixedMessage("!", "Module not found");
    }
}
