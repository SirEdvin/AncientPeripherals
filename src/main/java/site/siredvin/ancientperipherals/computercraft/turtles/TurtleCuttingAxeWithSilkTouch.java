package site.siredvin.ancientperipherals.computercraft.turtles;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class TurtleCuttingAxeWithSilkTouch extends TurtleCuttingAxe {

    public static ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "silk_cutting_axe");

    public TurtleCuttingAxeWithSilkTouch() {
        super(ID, TranslationUtil.turtle("silk_cutting_axe"), () -> {
            ItemStack craftingItem = new ItemStack(Items.CUTTING_AXE.get());
            craftingItem.enchant(Enchantments.SILK_TOUCH, 1);
            return craftingItem;
        });
    }

    @Override
    public ItemStack mimicTool() {
        ItemStack targetTool = super.mimicTool();
        targetTool.enchant(Enchantments.SILK_TOUCH, 1);
        return targetTool;
    }
}
