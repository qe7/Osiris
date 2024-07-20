package io.github.qe7.utils.math;

import io.github.qe7.utils.UtilityBase;

/**
 * Math utility class for math related operations
 */
public final class MathUtility extends UtilityBase {

    /**
     * Checks if the mouse is hovering over a rectangle
     *
     * @param x      the x position of the rectangle
     * @param y      the y position of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @return true if the mouse is hovering over the rectangle, false otherwise
     */
    public static boolean isHovered(float x, float y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * Rounds a value to it's nearest step value
     *
     * @param value the value to increment
     * @param step  the step to increment by
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the new value
     */
    public static double doStep(double value, double step, double min, double max) {
        double newValue = min + Math.round((value - min) / step) * step;
        if (newValue < min) newValue = min;
        if (newValue > max) newValue = max;
        return newValue;
    }
}
