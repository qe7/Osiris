package io.github.qe7.events.impl.game;

import lombok.Getter;

@Getter
public class KeyInputEvent {

    private final int keyCode;

    public KeyInputEvent(int keyCode) {
        this.keyCode = keyCode;
    }

}
