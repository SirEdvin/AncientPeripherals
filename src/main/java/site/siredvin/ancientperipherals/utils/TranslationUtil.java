package site.siredvin.ancientperipherals.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import site.siredvin.ancientperipherals.AncientPeripherals;

public class TranslationUtil {
    public static ITextComponent itemTooltip(String descriptionId) {
        return new TranslationTextComponent(String.format("%s.tooltip", descriptionId));
    }

    public static String turtle(String name) {
        return String.format("turtle.%s.%s", AncientPeripherals.MOD_ID, name);
    }
}
