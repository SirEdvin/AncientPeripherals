package site.siredvin.progressiveperipherals.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseNBTBlock;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.List;

public class FlexibleStatueItem extends BaseBlockItem {

    public FlexibleStatueItem() {
        super(Blocks.FLEXIBLE_STATUE.get());
    }

    @Override
    public @NotNull ITextComponent getName(ItemStack stack) {
        CompoundNBT internalData = stack.getTagElement(BaseNBTBlock.INTERNAL_DATA_TAG);
        if (internalData != null && internalData.contains(FlexibleStatueTileEntity.NAME_TAG))
            return new StringTextComponent(internalData.getString(FlexibleStatueTileEntity.NAME_TAG));
        return super.getName(stack);
    }

    @Override
    public void appendModHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT internalData = stack.getTagElement(BaseNBTBlock.INTERNAL_DATA_TAG);
        if (internalData != null && internalData.contains(FlexibleStatueTileEntity.AUTHOR_TAG)) {
            tooltip.add(TranslationUtil.localization("author").append(internalData.getString(FlexibleStatueTileEntity.AUTHOR_TAG)));
        } else {
            super.appendModHoverText(stack, worldIn, tooltip, flagIn);
        }
    }
}
