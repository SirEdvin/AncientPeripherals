package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class TurtleExtractingPickaxe extends TurtleDigTool {
    public static final String CORE_NAME = "extracting_pickaxe";
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, CORE_NAME);

    public TurtleExtractingPickaxe() {
        super(ID, TranslationUtil.turtle(CORE_NAME), Items.EXTRACTING_PICKAXE.get());
    }

    public TurtleExtractingPickaxe(ResourceLocation id, String adjective, Supplier<ItemStack> itemStackSup) {
        super(id, adjective, itemStackSup.get());
    }

    protected @Nullable BlockPos findOre(World world, BlockPos center) {
        Chunk chunk = world.getChunkAt(center);
        ChunkPos chunkPos = chunk.getPos();
        for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
            for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
                for (int y = 0; y < 256; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState block = chunk.getBlockState(pos);
                    if (block.getBlock().is(Tags.Blocks.ORES))
                        return pos;
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull ItemStack getMimicTool() {
        return new ItemStack(net.minecraft.item.Items.DIAMOND_PICKAXE);
    }

    @Override
    public @NotNull TurtleDigOperationType getOperationType(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return TurtleDigOperationType.EXTRACTING_PICKAXE;
    }

    @Override
    protected @NotNull Collection<BlockPos> detectTargetBlocks(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction, @NotNull World world) {
        BlockPos blockPosition = findOre(world, turtle.getPosition());
        if (blockPosition == null)
            return Collections.emptyList();
        if (world.isEmptyBlock(blockPosition) || WorldUtil.isLiquidBlock(world, blockPosition))
            return Collections.emptyList();
        return Collections.singletonList(blockPosition);
    }

    public static EnchantedTurtleExtractingPickaxe enchant(String prefix, Enchantment enchantment, int enchantmentLevel) {
        return new EnchantedTurtleExtractingPickaxe(prefix, enchantment, enchantmentLevel);
    }

    @Override
    protected boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableExtractingPickaxe;
    }

    public static class EnchantedTurtleExtractingPickaxe extends TurtleExtractingPickaxe {

        private final Enchantment enchantment;
        private final int enchantmentLevel;

        public EnchantedTurtleExtractingPickaxe(String prefix, Enchantment enchantment, int enchantmentLevel) {
            super(new ResourceLocation(ProgressivePeripherals.MOD_ID, prefix + CORE_NAME), TranslationUtil.turtle(prefix + CORE_NAME), () -> {
                ItemStack craftingItem = new ItemStack(Items.EXTRACTING_PICKAXE.get());
                craftingItem.enchant(enchantment, enchantmentLevel);
                return craftingItem;
            });
            this.enchantment = enchantment;
            this.enchantmentLevel = enchantmentLevel;
        }

        @Override
        public @NotNull ItemStack getMimicTool() {
            ItemStack targetTool = super.getMimicTool();
            targetTool.enchant(enchantment, enchantmentLevel);
            return targetTool;
        }
    }
}
