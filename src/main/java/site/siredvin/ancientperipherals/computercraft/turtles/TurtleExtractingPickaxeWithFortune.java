package site.siredvin.ancientperipherals.computercraft.turtles;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class TurtleExtractingPickaxeWithFortune extends TurtleExtractingPickaxe{
    public static ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "fortune_extracting_pickaxe");

    public TurtleExtractingPickaxeWithFortune() {
        super(ID, TranslationUtil.turtle("fortune_extracting_pickaxe"), () -> {
            ItemStack craftingItem = new ItemStack(Items.EXTRACTING_PICKAXE.get());
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
