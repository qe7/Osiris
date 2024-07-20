package io.github.qe7.managers.api;

import java.util.HashMap;
import java.util.Map;

public abstract class Manager<T, V> {

    private final Map<T, V> classMap = new HashMap<>();

    public abstract void register(T type);
    public abstract void unregister(T type);

    public Map<T, V> getMap() {
        return classMap;
    }
}
