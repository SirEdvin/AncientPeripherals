package site.siredvin.progressiveperipherals.common.items.base;

import de.srendi.advancedperipherals.common.util.EnumColor;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.List;

public class BaseBlockItem extends BlockItem {

    protected ITextComponent description;

    public BaseBlockItem(Block blockIn, Properties properties) {
        super(blockIn, properties.tab(ProgressivePeripherals.TAB));
    }

    public BaseBlockItem(Block blockIn) {
        this(blockIn, new Properties().stacksTo(1));
    }

    public void appendModHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ITextComponent modDescription = getModDescription();
        if (!modDescription.getString().isEmpty())
            tooltip.add(EnumColor.buildTextComponent(modDescription));
        if (!isEnabled())
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.disabled")));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        appendModHoverText(stack, worldIn, tooltip, flagIn);
    }

    public @NotNull ITextComponent getModDescription() {
        if (description == null)
            description = TranslationUtil.itemTooltip(getDescriptionId());
        return description;
    }

    public boolean isEnabled() {
        return true;
    }
}
