package site.siredvin.progressiveperipherals.common.items.base;

import de.srendi.advancedperipherals.common.util.EnumColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.List;
import java.util.function.Supplier;

public class BaseItem extends Item {
    protected ITextComponent description;
    Supplier<Boolean> enabledSup;

    public BaseItem() {
        this(new Item.Properties().stacksTo(1));
    }

    public BaseItem(Properties properties) {
        this(properties, () -> true);
    }

    public BaseItem(Properties properties, Supplier<Boolean> enabledSup) {
        super(properties.tab(ProgressivePeripherals.TAB));
        this.enabledSup = enabledSup;
    }

    public boolean isEnabled() {
        return enabledSup.get();
    }

    public void appendModHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(EnumColor.buildTextComponent(getModDescription()));
        if (!isEnabled())
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.disabled")));
    }

    public @NotNull ITextComponent getModDescription() {
        if (description == null)
            description = TranslationUtil.itemTooltip(getDescriptionId());
        return description;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        appendModHoverText(stack, worldIn, tooltip, flagIn);
    }
}
