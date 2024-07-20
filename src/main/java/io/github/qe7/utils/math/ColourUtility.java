package io.github.qe7.utils.math;

import io.github.qe7.utils.UtilityBase;

/**
 * Colour utility class
 */
public final class ColourUtility extends UtilityBase {

    /**
     * Gets a rainbow colour
     *
     * @param speed      the speed of the rainbow
     * @param saturation the saturation of the rainbow
     * @param brightness the brightness of the rainbow
     * @param offset     the offset of the rainbow
     * @return the rainbow colour
     */
    public static int getRainbow(int speed, float saturation, float brightness, int offset) {
        if (saturation < 0 || saturation > 1) throw new IllegalArgumentException("Saturation must be between 0 and 1");
        if (brightness < 0 || brightness > 1) throw new IllegalArgumentException("Brightness must be between 0 and 1");
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return java.awt.Color.getHSBColor(hue, saturation, brightness).getRGB();
    }
}
