package site.siredvin.progressiveperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class TurtleCuttingAxe extends TurtleDigTool {

    private static final Vector3i[] offsets = new Vector3i[]{
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1),
            new Vector3i(0, 1, 0),
            new Vector3i(0, -1, 0),
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
    };

    public static final String CORE_NAME = "cutting_axe";
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, CORE_NAME);

    public TurtleCuttingAxe() {
        super(ID, TranslationUtil.turtle(CORE_NAME), Items.CUTTING_AXE.get());
    }

    public TurtleCuttingAxe(ResourceLocation id, String adjective, Supplier<ItemStack> itemStackSup) {
        super(id, adjective, itemStackSup.get());
    }

    public ItemStack mimicTool() {
        return new ItemStack(net.minecraft.item.Items.DIAMOND_AXE);
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
        if (treeBlocks.size() >= ProgressivePeripheralsConfig.cuttingAxeMaxBlockCount)
            ProgressivePeripherals.LOGGER.info("Tree cutting stopped because of max size");
        TurtlePlayer turtlePlayer = TurtlePlayer.getWithPosition(turtle, turtlePosition, direction);
        turtlePlayer.loadInventory(mimicTool());
        for (BlockPos pos: treeBlocks) {
            digOneBlock(turtle, side, world, pos, turtlePlayer, turtleTile);
        }
        return TurtleCommandResult.success();
    }

    @Override
    protected boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableCuttingAxe;
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
        if (detectedBlocks.size() > ProgressivePeripheralsConfig.cuttingAxeMaxBlockCount)
            return;
        for (Vector3i direction: offsets) {
            BlockPos newPos = center.offset(direction);
            if (!detectedBlocks.contains(newPos)) {
                BlockState state = world.getBlockState(newPos);
                if (isLeaves(state) || (isLog(state) && isSame(state, referenceBlock))) {
                    if (detectedBlocks.size() > ProgressivePeripheralsConfig.cuttingAxeMaxBlockCount)
                        return;
                    detectedBlocks.add(newPos);
                    detectTree(world, newPos, detectedBlocks, referenceBlock);
                }
            }
        }
    }

    public static EnchantedTurtleCuttingAxe enchant(String prefix, Enchantment enchantment, int enchantmentLevel) {
        return new EnchantedTurtleCuttingAxe(prefix, enchantment, enchantmentLevel);
    }

    public static class EnchantedTurtleCuttingAxe extends TurtleCuttingAxe {

        private final Enchantment enchantment;
        private final int enchantmentLevel;

        public EnchantedTurtleCuttingAxe(String prefix, Enchantment enchantment, int enchantmentLevel) {
            super(new ResourceLocation(ProgressivePeripherals.MOD_ID, prefix + CORE_NAME), TranslationUtil.turtle(prefix + CORE_NAME), () -> {
                ItemStack craftingItem = new ItemStack(Items.CUTTING_AXE.get());
                craftingItem.enchant(enchantment, enchantmentLevel);
                return craftingItem;
            });
            this.enchantment = enchantment;
            this.enchantmentLevel = enchantmentLevel;
        }

        @Override
        public ItemStack mimicTool() {
            ItemStack targetTool = super.mimicTool();
            targetTool.enchant(enchantment, enchantmentLevel);
            return targetTool;
        }
    }
}
