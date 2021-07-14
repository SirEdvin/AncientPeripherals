/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2021. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;
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
import net.minecraft.block.LeavesBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class TurtleCuttingAxe extends TurtleTool {

    private static final Vector3i[] offsets = new Vector3i[]{
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1),
            new Vector3i(0, 1, 0),
            new Vector3i(0, -1, 0),
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
    };

    private static final ResourceLocation leaves = new ResourceLocation("minecraft","leaves");
    private static final ResourceLocation logs = new ResourceLocation("minecraft","logs");

    public static ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "cutting_axe");

    public TurtleCuttingAxe() {
        super(ID, TranslationUtil.turtle("cutting_axe"), Items.ABSTRACTIUM_AXE.get());
    }

    @Nonnull
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull Direction direction) {
        if (verb == TurtleVerb.DIG)
            return dig(turtle, side, direction);
        return TurtleCommandResult.failure("Unsupported action");
    }

    private boolean digOneBlock(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, World world, BlockPos blockPosition, TurtlePlayer turtlePlayer, TileEntity turtleTile) {
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

    protected TurtleCommandResult dig(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull Direction direction) {
        World world = turtle.getWorld();
        BlockPos turtlePosition = turtle.getPosition();
        TileEntity turtleTile = turtle instanceof TurtleBrain ? ((TurtleBrain)turtle).getOwner() : world.getBlockEntity(turtlePosition);
        if (turtleTile == null) {
            return TurtleCommandResult.failure("Turtle has vanished from existence.");
        }
        BlockPos blockPosition = turtlePosition.relative(direction);
        if (world.isEmptyBlock(blockPosition) || WorldUtil.isLiquidBlock(world, blockPosition))
            return TurtleCommandResult.failure("Nothing to dig here");
        BlockState state = world.getBlockState(blockPosition);
        if (!isLog(state))
            return TurtleCommandResult.failure("Nothing to dig here");
        Set<BlockPos> treeBlocks = new HashSet<>();
        treeBlocks.add(blockPosition);
        detectTree(world, blockPosition, treeBlocks, state);
        if (treeBlocks.size() >= AncientPeripheralsConfig.cuttingAxeMaxBlockCount)
            AncientPeripherals.LOGGER.info("Tree cutting stopped because of max size");
        TurtlePlayer turtlePlayer = TurtlePlayer.getWithPosition(turtle, turtlePosition, direction);
        turtlePlayer.loadInventory(this.item.copy());
        for (BlockPos pos: treeBlocks) {
            digOneBlock(turtle, side, world, pos, turtlePlayer, turtleTile);
        }
        return TurtleCommandResult.success();
    }

    private static Function<ItemStack, ItemStack> turtleDropConsumer(TileEntity tile, ITurtleAccess turtle) {
        return (drop) -> tile.isRemoved() ? drop : InventoryUtil.storeItems(drop, turtle.getItemHandler(), turtle.getSelectedSlot());
    }

    private static void stopConsuming(TileEntity tile, ITurtleAccess turtle) {
        Direction direction = tile.isRemoved() ? null : turtle.getDirection().getOpposite();
        DropConsumer.clearAndDrop(turtle.getWorld(), turtle.getPosition(), direction);
    }


    private static boolean isLog(BlockState state) {
        return (state.getBlock().is(BlockTags.LOGS)) || (state.getBlock().getTags().contains(logs));
    }

    private static boolean isSame(BlockState a, BlockState b) {
        return a.getBlock() == b.getBlock();
    }

    private static boolean isLeaves(BlockState state) {
        return state.getBlock() instanceof LeavesBlock || state.getBlock().getTags().contains(leaves);
    }

    private static void detectTree(World world, BlockPos center, Set<BlockPos> detectedBlocks, BlockState referenceBlock) {
        if (detectedBlocks.size() > AncientPeripheralsConfig.cuttingAxeMaxBlockCount)
            return;
        for (Vector3i direction: offsets) {
            BlockPos newPos = center.offset(direction);
            if (!detectedBlocks.contains(newPos)) {
                BlockState state = world.getBlockState(newPos);
                if (isLeaves(state) || (isLog(state) && isSame(state, referenceBlock))) {
                    if (detectedBlocks.size() > AncientPeripheralsConfig.cuttingAxeMaxBlockCount)
                        return;
                    detectedBlocks.add(newPos);
                    detectTree(world, newPos, detectedBlocks, referenceBlock);
                }
            }
        }
    }
}
