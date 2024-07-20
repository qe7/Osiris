package io.github.qe7.events.api;

import io.github.qe7.events.api.types.Event;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple event bus class for handling events and listeners
 *
 * @see Listener
 * @see EventBusPriorities
 *
 * @author Shae
 */
public class EventBus {

    private final Map<Class<? extends Event>, Map<EventBusPriorities, Set<Listener<? extends Event>>>> listeners = new ConcurrentHashMap<>();

    /**
     * Registers an object to the event bus
     *
     * @param object the object to register
     */
    public void register(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EventLink.class)) {
                EventBusPriorities priority = field.getAnnotation(EventLink.class).value();
                try {
                    Listener<? extends Event> listener = (Listener<? extends Event>) field.get(object);
                    Class<? extends Event> eventType = (Class<? extends Event>) ((java.lang.reflect.ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    listeners.computeIfAbsent(eventType, k -> new EnumMap<>(EventBusPriorities.class)).computeIfAbsent(priority, k -> new HashSet<>()).add(listener);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Unregisters an object from the event bus
     *
     * @param object the object to unregister
     */
    public void unregister(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EventLink.class)) {
                EventBusPriorities priority = field.getAnnotation(EventLink.class).value();
                try {
                    Listener<? extends Event> listener = (Listener<? extends Event>) field.get(object);
                    Class<? extends Event> eventType = (Class<? extends Event>) ((java.lang.reflect.ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    listeners.computeIfAbsent(eventType, k -> new EnumMap<>(EventBusPriorities.class)).computeIfAbsent(priority, k -> new HashSet<>()).remove(listener);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Posts an event to the event bus
     *
     * @param event the event to post
     */
    public void post(Event event) {
        Map<EventBusPriorities, Set<Listener<? extends Event>>> listenersByPriority = listeners.get(event.getClass());
        if (listenersByPriority != null) {
            for (EventBusPriorities priority : EventBusPriorities.values()) {
                Set<Listener<? extends Event>> listenersSet = listenersByPriority.get(priority);
                if (listenersSet != null) {
                    for (Listener listener : listenersSet) {
                        listener.onEvent(event);
                    }
                }
            }
        }
    }
}