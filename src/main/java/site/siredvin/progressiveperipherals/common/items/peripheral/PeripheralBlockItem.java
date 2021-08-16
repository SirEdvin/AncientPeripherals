package site.siredvin.progressiveperipherals.common.items.peripheral;

import de.srendi.advancedperipherals.common.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;

import java.util.function.Supplier;

public class PeripheralBlockItem extends BaseBlockItem {

    @Nullable ResourceLocation turtleID;
    @Nullable ResourceLocation pocketID;
    @NotNull Supplier<Boolean> enabledSup;

    public PeripheralBlockItem(@NotNull Block blockID, @NotNull Properties properties, @Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID, @NotNull Supplier<Boolean> enabledSup) {
        super(blockID, properties);
        this.turtleID = turtleID;
        this.pocketID = pocketID;
        this.enabledSup = enabledSup;
    }

    public PeripheralBlockItem(@NotNull Block blockID, @Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID, @NotNull Supplier<Boolean> enabledSup) {
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
            items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_ADVANCED, turtleID.toString()));
            items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_NORMAL, turtleID.toString()));
        }
        if (pocketID != null) {
            items.add(ItemUtil.makePocket(ItemUtil.POCKET_ADVANCED, pocketID.toString()));
            items.add(ItemUtil.makePocket(ItemUtil.POCKET_NORMAL, pocketID.toString()));
        }
    }
}
