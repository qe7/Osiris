package io.github.qe7.features.impl.modules.api.settings.impl.interfaces;

public interface IEnumSetting {
    String getName();
    Enum<?> getEnum(String name);
}
