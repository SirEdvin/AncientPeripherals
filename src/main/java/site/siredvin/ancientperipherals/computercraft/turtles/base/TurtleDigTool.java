package site.siredvin.ancientperipherals.computercraft.turtles.base;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.turtle.upgrades.TurtleTool;
import dan200.computercraft.shared.util.DropConsumer;
import dan200.computercraft.shared.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Function;

public abstract class TurtleDigTool extends TurtleTool {

    private final ItemStack craftingItemStack;

    public TurtleDigTool(ResourceLocation id, String adjective, ItemStack itemStack) {
        super(id, adjective, itemStack.getItem());
        craftingItemStack = itemStack;
    }
    public TurtleDigTool(ResourceLocation id, String adjective, Item item) {
        super(id, adjective, item);
        craftingItemStack = new ItemStack(item);
    }

    @Nonnull
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull Direction direction) {
        if (verb == TurtleVerb.DIG)
            return dig(turtle, side, direction);
        return TurtleCommandResult.failure("Unsupported action");
    }

    @Override
    public boolean isItemSuitable(@NotNull ItemStack stack) {
        if (!isEnabled())
            return false;
        CompoundNBT tag = stack.getTag();
        CompoundNBT targetTag = craftingItemStack.getTag();
        if (targetTag == null)
            return tag == null;
        if (tag == null)
            return false;
        return craftingItemStack.getTag().equals(stack.getTag());
    }

    protected abstract TurtleCommandResult dig(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull Direction direction);

    protected abstract boolean isEnabled();

    protected boolean digOneBlock(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, World world, BlockPos blockPosition, TurtlePlayer turtlePlayer, TileEntity turtleTile) {
        BlockState state = world.getBlockState(blockPosition);
        FluidState fluidState = world.getFluidState(blockPosition);
        if (ComputerCraft.turtlesObeyBlockProtection) {
            if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, blockPosition, state, turtlePlayer))) {
                return false;
            }

            if (!TurtlePermissions.isBlockEditable(world, blockPosition, turtlePlayer)) {
                return false;
            }
        }

        if (!this.canBreakBlock(state, world, blockPosition, turtlePlayer))
            return false;
        TurtleBlockEvent.Dig digEvent = new TurtleBlockEvent.Dig(turtle, turtlePlayer, world, blockPosition, state, this, side);
        if (MinecraftForge.EVENT_BUS.post(digEvent))
            return false;

        DropConsumer.set(world, blockPosition, turtleDropConsumer(turtleTile, turtle));
        TileEntity tile = world.getBlockEntity(blockPosition);
        world.levelEvent(2001, blockPosition, Block.getId(state));
        boolean canHarvest = state.canHarvestBlock(world, blockPosition, turtlePlayer);
        boolean canBreak = state.removedByPlayer(world, blockPosition, turtlePlayer, canHarvest, fluidState);
        if (canBreak) {
            state.getBlock().destroy(world, blockPosition, state);
        }

        if (canHarvest && canBreak) {
            state.getBlock().playerDestroy(world, turtlePlayer, blockPosition, state, tile, turtlePlayer.getMainHandItem());
        }

        stopConsuming(turtleTile, turtle);
        return true;
    }

    protected Function<ItemStack, ItemStack> turtleDropConsumer(TileEntity tile, ITurtleAccess turtle) {
        return (drop) -> tile.isRemoved() ? drop : InventoryUtil.storeItems(drop, turtle.getItemHandler(), turtle.getSelectedSlot());
    }

    protected void stopConsuming(TileEntity tile, ITurtleAccess turtle) {
        Direction direction = tile.isRemoved() ? null : turtle.getDirection().getOpposite();
        DropConsumer.clearAndDrop(turtle.getWorld(), turtle.getPosition(), direction);
    }
}
