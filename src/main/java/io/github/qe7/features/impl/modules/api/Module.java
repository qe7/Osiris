package io.github.qe7.features.impl.modules.api;

import com.google.gson.JsonObject;
import io.github.qe7.Osiris;
import io.github.qe7.features.impl.commands.api.Command;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.api.Setting;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.IntSetting;
import io.github.qe7.utils.configs.Serialized;
import io.github.qe7.utils.local.ChatUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public abstract class Module extends Command implements Serialized {

    private final ModuleCategory category;

    @Setter
    private String suffix;

    private boolean enabled;

    @Setter
    private int keyCode;

    public Module(final String name, final String description, final ModuleCategory category) {
        super(name, description);

        this.category = category;
    }

    public void onEnable() {
        Osiris.getInstance().getEventBus().register(this);
    }

    public void onDisable() {
        Osiris.getInstance().getEventBus().unregister(this);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (Objects.equals(this.getUsage(), "No usage provided")) {
                // Create new usage, if not provided (based off settings)
                // Formatted as "module <setting> <value>"
                StringBuilder usage = new StringBuilder(this.getName());

                System.out.println("Settings: " + Osiris.getInstance().getModuleManager().getSettingsByModule(this));

                for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByModule(this)) {
                    usage.append(" <").append(setting.getName().replace(" ", "")).append(">");
                }

                this.setUsage(usage.toString());

                ChatUtil.addPrefixedMessage("Settings", "Usage: " + this.getUsage());
            } else {
                ChatUtil.addPrefixedMessage("Settings", "Usage: " + this.getUsage());
            }
            return;
        }

        // I have sinned today father, forgive me for what I am about to do.
        if (args.length == 2 || args.length == 3) {
            for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByModule(this)) {
                if (!setting.getName().replace(" ", "").equalsIgnoreCase(args[1])) {
                    continue;
                }

                if (args.length == 2) {
                    ChatUtil.addPrefixedMessage("Settings", setting.getName() + ": " + setting.getValue());
                    return;
                }

                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting) setting;

                    if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
                        ChatUtil.addPrefixedMessage("Settings", "Invalid value");
                        return;
                    }

                    booleanSetting.setValue(Boolean.parseBoolean(args[2]));
                    ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + args[2]);
                    return;
                }
                if (setting instanceof IntSetting) {
                    IntSetting intSetting = (IntSetting) setting;

                    if (!args[2].matches("[0-9]+")) {
                        ChatUtil.addPrefixedMessage("Settings", "Invalid value");
                        return;
                    }

                    if (intSetting.getMinimum() != Integer.MIN_VALUE && Integer.parseInt(args[2]) < intSetting.getMinimum()) {
                        intSetting.setValue(intSetting.getMinimum());
                        ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + intSetting.getMinimum());
                        return;
                    }

                    if (intSetting.getMaximum() != Integer.MAX_VALUE && Integer.parseInt(args[2]) > intSetting.getMaximum()) {
                        intSetting.setValue(intSetting.getMaximum());
                        ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + intSetting.getMaximum());
                        return;
                    }

                    intSetting.setValue(Integer.parseInt(args[2]));
                    ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + args[2]);
                    return;
                }
                if (setting instanceof DoubleSetting) {
                    DoubleSetting doubleSetting = (DoubleSetting) setting;

                    if (!args[2].matches("[-+]?[0-9]*\\.?[0-9]+")) {
                        ChatUtil.addPrefixedMessage("Settings", "Invalid value");
                        return;
                    }

                    if (doubleSetting.getMinimum() != Double.MIN_VALUE && Double.parseDouble(args[2]) < doubleSetting.getMinimum()) {
                        doubleSetting.setValue(doubleSetting.getMinimum());
                        ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + doubleSetting.getMinimum());
                        return;
                    }

                    if (doubleSetting.getMaximum() != Double.MAX_VALUE && Double.parseDouble(args[2]) > doubleSetting.getMaximum()) {
                        doubleSetting.setValue(doubleSetting.getMaximum());
                        ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + doubleSetting.getMaximum());
                        return;
                    }

                    doubleSetting.setValue(Double.parseDouble(args[2]));
                    ChatUtil.addPrefixedMessage("Settings", "Set " + setting.getName() + " to " + args[2]);
                    return;
                }
                ChatUtil.addPrefixedMessage("Settings", "Invalid setting type");
                return;
            }
        }
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", this.enabled);
        object.addProperty("keyCode", this.keyCode);

        JsonObject settings = new JsonObject();

        for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByModule(this)) {
            settings.add(setting.getName(), setting.serialize());
        }

        object.add("settings", settings);

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.setEnabled(object.get("enabled").getAsBoolean());
        this.setKeyCode(object.get("keyCode").getAsInt());

        for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByModule(this)) {
            setting.deserialize(object.getAsJsonObject("settings").getAsJsonObject(setting.getName()));
        }
    }
}
