package site.siredvin.ancientperipherals.common.items;

import de.srendi.advancedperipherals.common.util.ItemUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import site.siredvin.ancientperipherals.AncientPeripherals;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PeripheralItem extends BaseItem {

    private final @Nullable String turtleID;
    private final @Nullable String pocketID;

    public PeripheralItem(Supplier<Boolean> enabledSup, @Nullable String turtleID, @Nullable String pocketID) {
        this(new Properties().stacksTo(1), enabledSup, turtleID, pocketID);
    }

    public PeripheralItem(Properties properties, Supplier<Boolean> enabledSup, @Nullable String turtleID, @Nullable String pocketID) {
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
                items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_ADVANCED, AncientPeripherals.MOD_ID + ":" + turtleID));
                items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_NORMAL, AncientPeripherals.MOD_ID + ":" + turtleID));
            }
            if (pocketID != null) {
                items.add(ItemUtil.makePocket(ItemUtil.POCKET_ADVANCED, AncientPeripherals.MOD_ID + ":" + pocketID));
                items.add(ItemUtil.makePocket(ItemUtil.POCKET_NORMAL, AncientPeripherals.MOD_ID + ":" + pocketID));
            }
        }
    }
}
