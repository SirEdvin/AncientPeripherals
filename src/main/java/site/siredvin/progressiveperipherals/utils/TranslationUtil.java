package site.siredvin.progressiveperipherals.utils;

import net.minecraft.util.text.*;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

public class TranslationUtil {
    public static TranslationTextComponent itemTooltip(String descriptionId) {
        return new TranslationTextComponent(String.format("%s.tooltip", descriptionId));
    }

    public static String turtle(String name) {
        return String.format("turtle.%s.%s", ProgressivePeripherals.MOD_ID, name);
    }

    public static String pocket(String name) {
        return String.format("pocket.%s.%s", ProgressivePeripherals.MOD_ID, name);
    }

    public static IFormattableTextComponent localization(String name) {
        return new TranslationTextComponent(String.format("text.%s.%s", ProgressivePeripherals.MOD_ID, name));
    }

    public static IFormattableTextComponent localization(String name, TextFormatting format) {
        return localization(name).withStyle(Style.EMPTY.withColor(Color.fromLegacyFormat(format)));
    }

    public static IFormattableTextComponent formattedLocalization(String name, Object... var1) {
        return new TranslationTextComponent(String.format("text.%s.%s", ProgressivePeripherals.MOD_ID, name), var1);
    }

    public static IFormattableTextComponent formattedLocalization(String name, TextFormatting format, Object... var1) {
        return formattedLocalization(name, var1).withStyle(Style.EMPTY.withColor(Color.fromLegacyFormat(format)));
    }
}
