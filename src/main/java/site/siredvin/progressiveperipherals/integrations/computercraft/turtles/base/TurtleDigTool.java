package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.client.TransformedModel;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.turtle.upgrades.TurtleTool;
import dan200.computercraft.shared.util.DropConsumer;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public abstract class TurtleDigTool extends AbstractTurtleUpgrade {

    private final ItemStack craftingItemStack;

    public TurtleDigTool(ResourceLocation id, String adjective, ItemStack itemStack) {
        super(id, TurtleUpgradeType.TOOL, adjective, itemStack.getItem());
        craftingItemStack = itemStack;
    }
    public TurtleDigTool(ResourceLocation id, String adjective, Item item) {
        super(id, TurtleUpgradeType.TOOL, adjective, item);
        craftingItemStack = new ItemStack(item);
    }

    public TurtleDigTool(ResourceLocation id, TurtleUpgradeType upgradeType, String adjective, ItemStack itemStack) {
        super(id, upgradeType, adjective, itemStack.getItem());
        craftingItemStack = itemStack;
    }
    public TurtleDigTool(ResourceLocation id, TurtleUpgradeType upgradeType, String adjective, Item item) {
        super(id, upgradeType, adjective, item);
        craftingItemStack = new ItemStack(item);
    }

    protected abstract @NotNull TurtleDigOperationType getOperationType();

    protected abstract boolean isEnabled();

    protected abstract @NotNull ItemStack getMimicTool();

    protected abstract @NotNull Collection<BlockPos> detectTargetBlocks(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction, @NotNull World world);

    protected boolean canBreakBlock(BlockState state, World world, BlockPos pos, TurtlePlayer player) {
        Block block = state.getBlock();
        return !state.isAir() && block != Blocks.BEDROCK && state.getDestroyProgress(player, world, pos) > 0.0F && block.canEntityDestroy(state, world, pos, player);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean consumeFuel(@NotNull ITurtleAccess turtle) {
        return turtle.consumeFuel(getOperationType().getCost());
    }

    @NotNull
    public TurtleCommandResult useTool(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull TurtleVerb verb, @NotNull Direction direction) {
        if (verb == TurtleVerb.DIG)
            return dig(turtle, side, direction);
        return TurtleCommandResult.failure("Unsupported action");
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public TransformedModel getModel(ITurtleAccess turtle, @Nonnull TurtleSide side) {
        float xOffset = side == TurtleSide.LEFT ? -0.40625F : 0.40625F;
        Matrix4f transform = new Matrix4f(new float[]{0.0F, 0.0F, -1.0F, 1.0F + xOffset, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F});
        return TransformedModel.of(this.getCraftingItem(), new TransformationMatrix(transform));
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

    protected TurtleCommandResult dig(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction) {
        World world = turtle.getWorld();
        BlockPos turtlePosition = turtle.getPosition();
        TileEntity turtleTile = turtle instanceof TurtleBrain ? ((TurtleBrain)turtle).getOwner() : world.getBlockEntity(turtlePosition);
        if (turtleTile == null) {
            return TurtleCommandResult.failure("Turtle has vanished from existence.");
        }
        BlockPos blockPosition = turtlePosition.relative(direction);
        TurtlePlayer turtlePlayer = TurtlePlayer.getWithPosition(turtle, turtlePosition, direction);
        turtlePlayer.loadInventory(getMimicTool());
        if (!consumeFuel(turtle))
            return TurtleCommandResult.failure("Not enough fuel");
        Collection<BlockPos> targetBlocks = detectTargetBlocks(turtle, side, direction, world);
        if (targetBlocks.isEmpty())
            return TurtleCommandResult.failure("Nothing to dig here");
        for (BlockPos targetBlock: targetBlocks) {
            if (!digOneBlock(turtle, side, world, targetBlock, turtlePlayer, turtleTile))
                return TurtleCommandResult.failure();
        }
        return TurtleCommandResult.success();
    }

    protected boolean digOneBlock(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, World world, BlockPos blockPosition, TurtlePlayer turtlePlayer, TileEntity turtleTile) {
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

        boolean canHarvest = state.canHarvestBlock(world, blockPosition, turtlePlayer);
        boolean canBreak = state.removedByPlayer(world, blockPosition, turtlePlayer, canHarvest, fluidState);

        DropConsumer.set(world, blockPosition, turtleDropConsumer(turtleTile, turtle));
        TileEntity tile = world.getBlockEntity(blockPosition);
        world.levelEvent(2001, blockPosition, Block.getId(state));

        if (canBreak) {
            state.getBlock().destroy(world, blockPosition, state);
            if (canHarvest)
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
