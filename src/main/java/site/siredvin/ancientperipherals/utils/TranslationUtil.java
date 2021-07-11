package site.siredvin.ancientperipherals.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import site.siredvin.ancientperipherals.AncientPeripherals;

public class TranslationUtil {
    public static ITextComponent itemTooltip(String descriptionId) {
        return new TranslationTextComponent(String.format("%s.tooltip", descriptionId));
    }
}
