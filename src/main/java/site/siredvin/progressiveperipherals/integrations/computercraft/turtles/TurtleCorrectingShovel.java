package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.*;
import java.util.function.Function;

public class TurtleCorrectingShovel extends TurtleDigTool {

    public static final String CORE_NAME = "correcting_shovel";
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, CORE_NAME);

    private static final Map<ResourceLocation, Map<ITag<Item>, ItemStack>> DIMENSIONAL_DROP_MAPPING_TAGS = new HashMap<>();
    private static final Map<ResourceLocation, Map<Item, ItemStack>> DIMENSIONAL_DROP_MAPPING_BLOCKS = new HashMap<>();

    public TurtleCorrectingShovel() {
        super(ID, TranslationUtil.turtle(CORE_NAME), Items.CORRECTING_SHOVEL.get());
    }

    @Override
    public @NotNull ItemStack getMimicTool() {
        return new ItemStack(net.minecraft.item.Items.DIAMOND_SHOVEL);
    }

    @Override
    public @NotNull TurtleDigOperationType getOperationType() {
        return TurtleDigOperationType.CORRECTING_SHOVEL;
    }

    @Override
    protected @NotNull Collection<BlockPos> detectTargetBlocks(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction, @NotNull World world) {
        BlockPos blockPosition = turtle.getPosition().relative(direction);
        if (world.isEmptyBlock(blockPosition) || WorldUtil.isLiquidBlock(world, blockPosition))
            return Collections.emptyList();
        return Collections.singletonList(blockPosition);
    }

    @Override
    protected Function<ItemStack, ItemStack> turtleDropConsumer(TileEntity tile, ITurtleAccess turtle) {
        return (drop) -> {
            if (tile.isRemoved())
                return drop;
            if (tile.getLevel() == null)
                return InventoryUtil.storeItems(drop, turtle.getItemHandler(), turtle.getSelectedSlot());
            ResourceLocation dimension = tile.getLevel().dimension().location();
            if (DIMENSIONAL_DROP_MAPPING_BLOCKS.containsKey(dimension)) {
                Optional<Map.Entry<Item, ItemStack>> mapping = DIMENSIONAL_DROP_MAPPING_BLOCKS.get(dimension).entrySet().stream().filter(entry -> drop.getItem() == entry.getKey()).findAny();
                if (mapping.isPresent()) {
                    ItemStack newStack = mapping.get().getValue().copy();
                    newStack.setCount(drop.getCount());
                    return InventoryUtil.storeItems(newStack, turtle.getItemHandler(), turtle.getSelectedSlot());
                }
            }
            if (DIMENSIONAL_DROP_MAPPING_TAGS.containsKey(dimension)) {
                Optional<Map.Entry<ITag<Item>, ItemStack>> mapping = DIMENSIONAL_DROP_MAPPING_TAGS.get(dimension).entrySet().stream().filter(entry -> drop.getItem().is(entry.getKey())).findAny();
                if (mapping.isPresent()) {
                    ItemStack newStack = mapping.get().getValue().copy();
                    newStack.setCount(drop.getCount());
                    return InventoryUtil.storeItems(newStack, turtle.getItemHandler(), turtle.getSelectedSlot());
                }
            }
            return InventoryUtil.storeItems(drop, turtle.getItemHandler(), turtle.getSelectedSlot());
        };
    }

    @Override
    protected boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableCorrectingShovel;
    }

    public static void addResourceMapping(ResourceLocation dimension, ITag<Item> source, ItemStack target) {
        if (!DIMENSIONAL_DROP_MAPPING_TAGS.containsKey(dimension))
            DIMENSIONAL_DROP_MAPPING_TAGS.put(dimension, new HashMap<>());
        DIMENSIONAL_DROP_MAPPING_TAGS.get(dimension).put(source, target);
    }
    public static void addResourceMapping(ResourceLocation dimension, Item source, ItemStack target) {
        if (!DIMENSIONAL_DROP_MAPPING_BLOCKS.containsKey(dimension))
            DIMENSIONAL_DROP_MAPPING_BLOCKS.put(dimension, new HashMap<>());
        DIMENSIONAL_DROP_MAPPING_BLOCKS.get(dimension).put(source, target);
    }
}
