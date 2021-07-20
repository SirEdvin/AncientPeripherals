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

    private final @Nullable ResourceLocation turtleID;
    private final @Nullable ResourceLocation pocketID;

    public PeripheralItem(Supplier<Boolean> enabledSup, @Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID) {
        this(new Properties().stacksTo(1), enabledSup, turtleID, pocketID);
    }

    public PeripheralItem(Properties properties, Supplier<Boolean> enabledSup, @Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID) {
        super(properties, enabledSup);
        this.turtleID = turtleID;
        this.pocketID = pocketID;
    }

    @Override
    public void fillItemCategory(@NotNull ItemGroup group, @NotNull NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (!allowdedIn(group))
            return;
        if (isEnabled()) {
            if (turtleID != null) {
                items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_ADVANCED, turtleID.toString()));
                items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_NORMAL, turtleID.toString()));
            }
            if (pocketID != null) {
                items.add(ItemUtil.makePocket(ItemUtil.POCKET_ADVANCED, pocketID.toString()));
                items.add(ItemUtil.makePocket(ItemUtil.POCKET_NORMAL, pocketID.toString()));
            }
        }
    }
}
