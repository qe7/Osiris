package io.github.qe7.features.modules.api.settings.api;

import io.github.qe7.utils.configs.Serialized;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;

public abstract class Setting<T> implements Serialized {

    private BooleanSupplier supplier;

    @Getter
    private final T defaultValue;
    @Setter
    @Getter
    private T value;

    @Getter
    private final String name;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public boolean shouldHide() {
        return supplier != null && !supplier.getAsBoolean();
    }

    public <V extends Setting<?>> V supplyIf(BooleanSupplier supplier) {
        this.supplier = supplier;
        return (V) this;
    }
}
