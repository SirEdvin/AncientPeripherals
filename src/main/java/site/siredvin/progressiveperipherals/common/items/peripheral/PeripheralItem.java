package site.siredvin.progressiveperipherals.common.items.peripheral;

import de.srendi.advancedperipherals.common.util.ItemUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.items.base.BaseItem;

import java.util.function.Supplier;

public class PeripheralItem extends BaseItem {

    public PeripheralItem(Supplier<Boolean> enabledSup) {
        this(new Properties().stacksTo(1), enabledSup);
    }

    public PeripheralItem(Properties properties, Supplier<Boolean> enabledSup) {
        super(properties, enabledSup);
    }
}
