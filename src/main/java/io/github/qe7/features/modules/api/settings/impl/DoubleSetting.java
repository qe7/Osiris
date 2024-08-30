package io.github.qe7.features.modules.api.settings.impl;

import com.google.gson.JsonObject;
import io.github.qe7.features.modules.api.settings.api.Setting;
import lombok.Getter;

@Getter
public final class DoubleSetting extends Setting<Double> {

    private final double minimum, maximum, step;

    public DoubleSetting(String name, Double defaultValue, double minimum, double maximum, double step) {
        super(name, defaultValue);
        this.minimum = minimum;
        this.maximum = maximum;
        this.step = step;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("value", this.getValue());

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.setValue(object.get("value").getAsDouble());
    }
}
