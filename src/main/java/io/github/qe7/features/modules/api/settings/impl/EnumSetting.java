package io.github.qe7.features.modules.api.settings.impl;

import com.google.gson.JsonObject;
import io.github.qe7.features.modules.api.settings.api.Setting;
import io.github.qe7.features.modules.api.settings.impl.interfaces.IEnumSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EnumSetting<T extends IEnumSetting> extends Setting<T> {

    private final List<IEnumSetting> values;

    public EnumSetting(String name, T defaultValue) {
        super(name, defaultValue);
        this.values = new ArrayList<>(Arrays.asList(defaultValue.getClass().getEnumConstants()));
    }

    public List<IEnumSetting> getValues() {
        return values;
    }

    public int getIndex() {
        return values.indexOf(getValue());
    }

    public void setIndex(int index) {
        if (index < values.size()) {
            setValue((T) values.get(index));
        }
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("value", getIndex());

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        setIndex(object.get("value").getAsInt());
    }
}
