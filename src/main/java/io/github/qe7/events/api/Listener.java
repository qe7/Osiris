package io.github.qe7.events.api;

import io.github.qe7.events.api.types.Event;

@FunctionalInterface
public interface Listener<T extends Event> {
    void onEvent(T event);
}