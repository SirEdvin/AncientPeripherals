package site.siredvin.progressiveperipherals.common.items;

import de.srendi.advancedperipherals.AdvancedPeripherals;
import de.srendi.advancedperipherals.common.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PeripheralBlockItem extends BaseBlockItem {

    @Nullable String turtleID;
    @Nullable String pocketID;
    @Nonnull Supplier<Boolean> enabledSup;

    public PeripheralBlockItem(@Nonnull Block blockID, @Nonnull Properties properties, @Nullable String turtleID, @Nullable String pocketID, @Nonnull Supplier<Boolean> enabledSup) {
        super(blockID, properties);
        this.turtleID = turtleID;
        this.pocketID = pocketID;
        this.enabledSup = enabledSup;
    }

    public PeripheralBlockItem(@Nonnull Block blockID, @Nullable String turtleID, @Nullable String pocketID, @Nonnull Supplier<Boolean> enabledSup) {
        super(blockID);
        this.turtleID = turtleID;
        this.pocketID = pocketID;
        this.enabledSup = enabledSup;
    }

    @Override
    public boolean isEnabled() {
        return enabledSup.get();
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (!allowdedIn(group))
            return;
        if (turtleID != null) {
            items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_ADVANCED, AdvancedPeripherals.MOD_ID + ":" + turtleID));
            items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_NORMAL, AdvancedPeripherals.MOD_ID + ":" + turtleID));
        }
        if (pocketID != null) {
            items.add(ItemUtil.makePocket(ItemUtil.POCKET_ADVANCED, AdvancedPeripherals.MOD_ID + ":" + pocketID));
            items.add(ItemUtil.makePocket(ItemUtil.POCKET_NORMAL, AdvancedPeripherals.MOD_ID + ":" + pocketID));
        }
    }
}
