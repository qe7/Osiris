package io.github.qe7.utils.render;

import io.github.qe7.utils.UtilBase;

import java.awt.*;

/**
 * Colour utility class
 */
public final class ColourUtil extends UtilBase {

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

    /**
     * Mixes two colours together based on a percentage.
     *
     * @param color1 The first colour
     * @param color2 The second colour
     * @param percent The percentage of the first colour
     * @return The mixed colour
     */
    public static Color mixColors(final Color color1, final Color color2, final double percent) {
        final double inverse_percent = 1.0 - percent;
        final int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        final int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        final int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

    /**
     * Gets a mixed colour based on the index.
     *
     * @param index The index
     * @return The mixed colour
     */
    public static Color getMixedColor(final Color color1, final Color color2, final int index) {
        return ColourUtil.mixColors(color1, color2, (float) (Math.sin((System.currentTimeMillis() + index * 100) / 500.0) + 1.0) / 2.0f);
    }
}
