package site.siredvin.ancientperipherals.utils;

import net.minecraft.util.text.TranslationTextComponent;
import site.siredvin.ancientperipherals.AncientPeripherals;

public class TranslationUtil {
    public static TranslationTextComponent itemTooltip(String descriptionId) {
        return new TranslationTextComponent(String.format("%s.tooltip", descriptionId));
    }

    public static String turtle(String name) {
        return String.format("turtle.%s.%s", AncientPeripherals.MOD_ID, name);
    }

    public static TranslationTextComponent localization(String name) {
        return  new TranslationTextComponent(String.format("text.%s.%s", AncientPeripherals.MOD_ID, name));
    }
}
