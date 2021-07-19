package site.siredvin.progressiveperipherals.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseNBTBlock;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.List;

public class FlexibleRealityAnchorItem extends BaseBlockItem {

    public FlexibleRealityAnchorItem() {
        super(Blocks.FLEXIBLE_REALITY_ANCHOR.get());
    }

    @Override
    public void appendModHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendModHoverText(stack, worldIn, tooltip, flagIn);
        CompoundNBT internalData = stack.getTagElement(BaseNBTBlock.INTERNAL_DATA_TAG);
        if (internalData != null && !internalData.isEmpty())
            tooltip.add(TranslationUtil.localization("configured"));
    }
}
