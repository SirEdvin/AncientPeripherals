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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import javax.annotation.Nullable;
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

    public ItemStack mimicTool() {
        return new ItemStack(net.minecraft.item.Items.DIAMOND_PICKAXE);
    }

    @Override
    protected TurtleCommandResult dig(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction) {
        World world = turtle.getWorld();
        BlockPos turtlePosition = turtle.getPosition();
        TileEntity turtleTile = turtle instanceof TurtleBrain ? ((TurtleBrain)turtle).getOwner() : world.getBlockEntity(turtlePosition);
        if (turtleTile == null) {
            return TurtleCommandResult.failure("Turtle has vanished from existence.");
        }
        // find block here
        BlockPos blockPosition = findOre(world, turtlePosition);
        if (blockPosition == null)
            return TurtleCommandResult.failure("Nothing to dig here");
        if (world.isEmptyBlock(blockPosition) || WorldUtil.isLiquidBlock(world, blockPosition))
            return TurtleCommandResult.failure("Nothing to dig here");
        TurtlePlayer turtlePlayer = TurtlePlayer.getWithPosition(turtle, turtlePosition, direction);
        turtlePlayer.loadInventory(mimicTool());
        if (!digOneBlock(turtle, side, world, blockPosition, turtlePlayer, turtleTile))
            return TurtleCommandResult.failure();
        return TurtleCommandResult.success();
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
        public ItemStack mimicTool() {
            ItemStack targetTool = super.mimicTool();
            targetTool.enchant(enchantment, enchantmentLevel);
            return targetTool;
        }
    }
}
