package site.siredvin.progressiveperipherals.utils;

import java.awt.*;

public class ColorUtils {
    public static boolean isLight(Color color) {
        int r = color.getRed();
        int b = color.getBlue();
        int g = color.getGreen();
        double hsp = Math.sqrt(0.299 * (r * r) + 0.587 * (g * g) + 0.114 * (b * b));
        return hsp>127.5;
    }

    public static boolean isDark(Color color) {
        return !isLight(color);
    }

//    public Color brighter() {
//        int var1 = this.getRed();
//        int var2 = this.getGreen();
//        int var3 = this.getBlue();
//        int var4 = this.getAlpha();
//        byte var5 = 3;
//        if (var1 == 0 && var2 == 0 && var3 == 0) {
//            return new Color(var5, var5, var5, var4);
//        } else {
//            if (var1 > 0 && var1 < var5) {
//                var1 = var5;
//            }
//
//            if (var2 > 0 && var2 < var5) {
//                var2 = var5;
//            }
//
//            if (var3 > 0 && var3 < var5) {
//                var3 = var5;
//            }
//
//            return new Color(Math.min((int)((double)var1 / 0.7D), 255), Math.min((int)((double)var2 / 0.7D), 255), Math.min((int)((double)var3 / 0.7D), 255), var4);
//        }
//    }
//
//    public Color darker() {
//        return new Color(Math.max((int)((double)this.getRed() * 0.7D), 0), Math.max((int)((double)this.getGreen() * 0.7D), 0), Math.max((int)((double)this.getBlue() * 0.7D), 0), this.getAlpha());
//    }
}
