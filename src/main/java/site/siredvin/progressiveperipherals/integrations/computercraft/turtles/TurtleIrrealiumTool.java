package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.shared.util.InventoryUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.IrrealiumToolPeripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.dataproxy.IrrealiumToolDataProxy;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class TurtleIrrealiumTool extends TurtleDigTool {

    protected TurtleIrrealiumTool(ResourceLocation id, String adjective, ItemStack itemStack) {
        super(id, TurtleUpgradeType.BOTH, adjective, itemStack);
    }

    @Override
    public @NotNull TurtleDigOperationType getOperationType(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return IrrealiumToolDataProxy.getOperationType(turtle, side);
    }

    @Override
    protected boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableIrrealiumTools;
    }

    @Override
    protected @NotNull ItemStack getMimicTool() {
        return getCraftingItem();
    }

    @Override
    protected @NotNull Collection<BlockPos> detectTargetBlocks(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction, @NotNull World world) {
        return IrrealiumToolDataProxy.getUltimineMode(turtle, side).getMiningArea(direction, turtle.getDirection(), turtle.getPosition().relative(direction));
    }

    @Override
    protected Function<ItemStack, ItemStack> turtleDropConsumer(@NotNull TileEntity tile, @NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return (drop) -> {
            if (tile.isRemoved())
                return drop;
            if (IrrealiumToolDataProxy.isVoiding(turtle, side)) {
                Set<ResourceLocation> itemTags = drop.getItem().getTags();
                for (String voidTag: IrrealiumToolDataProxy.getVoidingTags(turtle, side))
                    if (itemTags.contains(new ResourceLocation(voidTag)))
                        return ItemStack.EMPTY;
            }
            return InventoryUtil.storeItems(drop, turtle.getItemHandler(), turtle.getSelectedSlot());
        };
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new IrrealiumToolPeripheral(turtle, side);
    }

    public static TurtleIrrealiumTool build(String name, Item baseItem) {
        ItemStack sourceStack = new ItemStack(baseItem);
        ResourceLocation id = new ResourceLocation(ProgressivePeripherals.MOD_ID, name);
        return new TurtleIrrealiumTool(id, TranslationUtil.turtle(name), sourceStack);
    }

    public static TurtleIrrealiumTool build(String name, Item baseItem, Enchantment enchantment, int enchantmentLevel) {
        ItemStack sourceStack = new ItemStack(baseItem);
        sourceStack.enchant(enchantment, enchantmentLevel);
        ResourceLocation id = new ResourceLocation(ProgressivePeripherals.MOD_ID, name);
        return new TurtleIrrealiumTool(id, TranslationUtil.turtle(name), sourceStack);
    }
}
