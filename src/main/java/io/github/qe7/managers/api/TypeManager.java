package io.github.qe7.managers.api;

import java.util.HashMap;
import java.util.Map;

public abstract class TypeManager<T> {

    private final Map<Class<? extends T>, T> classMap = new HashMap<>();

    public void register(Class<? extends T> clazz) {
        try {
            classMap.putIfAbsent(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            System.out.println("Failed to register class: " + clazz.getSimpleName() + " - " + e.getMessage());
            throw new RuntimeException("Failed to register class: " + clazz.getSimpleName() + " - " + e.getMessage());
        }
    }

    public void unregister(Class<? extends T> clazz) {
        classMap.remove(clazz);
    }

    public Map<Class<? extends T>, T> getMap() {
        return classMap;
    }
}
