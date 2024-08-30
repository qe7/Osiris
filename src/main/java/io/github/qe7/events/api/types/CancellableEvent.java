package io.github.qe7.events.api.types;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancellableEvent extends Event {
    private boolean cancelled;
}
