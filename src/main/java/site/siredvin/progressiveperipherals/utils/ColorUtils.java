package site.siredvin.progressiveperipherals.utils;

import java.awt.*;

public class ColorUtils {
    public static Color swapAlpha(Color color, int newAlpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
    }
}
