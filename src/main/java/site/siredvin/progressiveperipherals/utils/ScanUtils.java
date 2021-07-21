package site.siredvin.progressiveperipherals.utils;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ScanUtils {
    public static void relativeTraverseBlocks(World world, BlockPos center, int radius, BiConsumer<BlockState, BlockPos> consumer) {
        traverseBlocks(world, center, radius, consumer, true);
    }

    public static void traverseBlocks(World world, BlockPos center, int radius, BiConsumer<BlockState, BlockPos> consumer) {
        traverseBlocks(world, center, radius, consumer, false);
    }

    public static void traverseBlocks(World world, BlockPos center, int radius, BiConsumer<BlockState, BlockPos> consumer, boolean relativePosition) {
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();
        for (int oX = x - radius; oX <= x + radius; oX++) {
            for (int oY = y - radius; oY <= y + radius; oY++) {
                for (int oZ = z - radius; oZ <= z + radius; oZ++) {
                    BlockPos subPos = new BlockPos(oX, oY, oZ);
                    BlockState blockState = world.getBlockState(subPos);
                    if (!blockState.isAir(world, subPos)) {
                        if (relativePosition) {
                            consumer.accept(blockState, new BlockPos(oX - x, oY - y, oZ - z));
                        } else {
                            consumer.accept(blockState, subPos);
                        }
                    }
                }
            }
        }
    }

    public static @Nullable Pair<BlockPos, BlockState> findBlockInRadius(World world, BlockPos center, int radius, Predicate<BlockState> predicate) {
        ValueContainer<Pair<BlockPos, BlockState>> container = new ValueContainer<>();
        traverseBlocks(world, center, radius, ((blockState, blockPos) -> {
            if (predicate.test(blockState))
                container.setValue(Pair.of(blockPos, blockState));
        }));
        return container.getValue();
    }
}
