package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.BlockState;
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
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.turtles.base.TurtleDigTool;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

import javax.annotation.Nullable;

public class TurtleExtractingPickaxe extends TurtleDigTool {
    public static ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "extracting_pickaxe");

    public TurtleExtractingPickaxe() {
        super(ID, TranslationUtil.turtle("extracting_pickaxe"), Items.EXTRACTING_PICKAXE.get());
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
        turtlePlayer.loadInventory(new ItemStack(net.minecraft.item.Items.DIAMOND_PICKAXE));
        if (!digOneBlock(turtle, side, world, blockPosition, turtlePlayer, turtleTile))
            return TurtleCommandResult.failure();
        return TurtleCommandResult.success();
    }

    @Override
    protected boolean isEnabled() {
        return AncientPeripheralsConfig.enableExtractingPickaxe;
    }
}
