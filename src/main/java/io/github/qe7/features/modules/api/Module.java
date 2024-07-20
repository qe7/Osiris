package io.github.qe7.features.modules.api;

import com.google.gson.JsonObject;
import io.github.qe7.Osiris;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.api.Setting;
import io.github.qe7.utils.configs.Serialized;

public abstract class Module implements Serialized {

    private final String name, description;

    private final ModuleCategory category;

    private String suffix;

    private boolean enabled;

    private int keyCode;

    public Module(final String name, final String description, final ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable() {
        Osiris.getInstance().getEventBus().register(this);
    }

    public void onDisable() {
        Osiris.getInstance().getEventBus().unregister(this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", this.enabled);
        object.addProperty("keyCode", this.keyCode);

        JsonObject settings = new JsonObject();

        for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByFeature(this)) {
            settings.add(setting.getName(), setting.serialize());
        }

        object.add("settings", settings);

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.setEnabled(object.get("enabled").getAsBoolean());
        this.setKeyCode(object.get("keyCode").getAsInt());

        for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByFeature(this)) {
            setting.deserialize(object.getAsJsonObject("settings").getAsJsonObject(setting.getName()));
        }
    }
}
