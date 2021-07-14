package site.siredvin.ancientperipherals.computercraft.turtles;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class TurtleCuttingAxeWithFortune extends TurtleCuttingAxe {

    public static ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "fortune_cutting_axe");

    public TurtleCuttingAxeWithFortune() {
        super(ID, TranslationUtil.turtle("fortune_cutting_axe"), () -> {
            ItemStack craftingItem = new ItemStack(Items.CUTTING_AXE.get());
            craftingItem.enchant(Enchantments.BLOCK_FORTUNE, 3);
            return craftingItem;
        });
    }

    @Override
    public ItemStack mimicTool() {
        ItemStack targetTool = super.mimicTool();
        targetTool.enchant(Enchantments.BLOCK_FORTUNE, 3);
        return targetTool;
    }
}
