package io.github.qe7.features.impl.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.impl.commands.api.Command;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.utils.local.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "Test command");

        this.getAliases().add("B");
        this.setUsage("bind <module> <key>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            ChatUtil.addPrefixedMessage("Bind", "Invalid arguments");
            ChatUtil.addPrefixedMessage("Bind", "Usage: " + this.getUsage());
            return;
        }

        final String moduleName = args[1];
        final String key = args[2];

        Module module = null;
        
        for (Module m : Osiris.getInstance().getModuleManager().getMap().values()) {
            if (m.getName().replace(" ", "").equalsIgnoreCase(moduleName)) {
                module = m;
                break;
            }
        }
        
        if (module == null) {
            ChatUtil.addPrefixedMessage("Bind", "Module not found");
            return;
        }

        if (key.equalsIgnoreCase("none")) {
            module.setKeyCode(-1);
            ChatUtil.addPrefixedMessage("Bind", "Unbound " + module.getName());
            return;
        }

        int keyCode;

        try {
            keyCode = Keyboard.getKeyIndex(key.toUpperCase());
        } catch (NumberFormatException e) {
            ChatUtil.addPrefixedMessage("Bind", "Invalid key");
            return;
        }

        module.setKeyCode(keyCode);
    }
}
