package site.siredvin.progressiveperipherals.common.items;

import de.srendi.advancedperipherals.common.util.EnumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BaseItem extends Item {
    ITextComponent description;
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

    @Override
    public ITextComponent getDescription() {
        if (description == null)
            description = TranslationUtil.itemTooltip(getDescriptionId());
        return description;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.hold_ctrl")));
        } else {
            tooltip.add(EnumColor.buildTextComponent(getDescription()));
        }
        if (!isEnabled())
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.disabled")));
    }
}
