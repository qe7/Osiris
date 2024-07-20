package io.github.qe7.events.impl.game;

import io.github.qe7.events.api.types.Event;

public class KeyInputEvent extends Event {

    private final int keyCode;

    public KeyInputEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
