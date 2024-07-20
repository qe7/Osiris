package io.github.qe7.events.api.types;

public class CancellableEvent extends Event {

    private boolean cancelled;

    /**
     * @return whether the event is cancelled.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets the event to be cancelled.
     *
     * @param cancelled whether the event is cancelled.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
