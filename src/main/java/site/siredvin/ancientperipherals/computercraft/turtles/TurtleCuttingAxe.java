package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class TurtleCuttingAxe extends TurtleDigTool {

    private static final Vector3i[] offsets = new Vector3i[]{
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1),
            new Vector3i(0, 1, 0),
            new Vector3i(0, -1, 0),
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
    };

    public static ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "cutting_axe");

    public TurtleCuttingAxe() {
        super(ID, TranslationUtil.turtle("cutting_axe"), Items.CUTTING_AXE.get());
    }

    @Override
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
        turtlePlayer.loadInventory(new ItemStack(net.minecraft.item.Items.DIAMOND_AXE));
        for (BlockPos pos: treeBlocks) {
            digOneBlock(turtle, side, world, pos, turtlePlayer, turtleTile);
        }
        return TurtleCommandResult.success();
    }

    @Override
    protected boolean isEnabled() {
        return AncientPeripheralsConfig.enableCuttingAxe;
    }

    private static boolean isLog(BlockState state) {
        return state.getBlock().is(BlockTags.LOGS);
    }

    private static boolean isSame(BlockState a, BlockState b) {
        return a.getBlock() == b.getBlock();
    }

    private static boolean isLeaves(BlockState state) {
        return state.getBlock().is(BlockTags.LEAVES);
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
