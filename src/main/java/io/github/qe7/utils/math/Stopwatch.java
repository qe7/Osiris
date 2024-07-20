package io.github.qe7.utils.math;

/**
 * Stopwatch utility class
 */
public class Stopwatch {

    // Last time the timer was reset
    private long lastMS = 0L;

    /**
     * Checks if the timer has reached the specified milliseconds
     *
     * @param milliseconds the milliseconds to check
     * @return true if the timer has reached the specified milliseconds, false otherwise
     */
    public boolean elapsed(long milliseconds) {
        return System.currentTimeMillis() - this.lastMS >= milliseconds;
    }

    /**
     * Resets the timer
     */
    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }
}
