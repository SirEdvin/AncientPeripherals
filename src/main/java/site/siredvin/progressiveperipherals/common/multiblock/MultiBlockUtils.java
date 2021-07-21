package site.siredvin.progressiveperipherals.common.multiblock;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.api.tileentity.IMultiBlockController;
import site.siredvin.progressiveperipherals.utils.ScanUtils;

import java.util.function.Predicate;

public class MultiBlockUtils {
    public static int calculateSquare(BlockPos northWestCorner, BlockPos southEastCorner) {
        int zDiff = southEastCorner.getZ() - northWestCorner.getZ();
        int xDiff = southEastCorner.getX() - northWestCorner.getX();
        int yDiff = southEastCorner.getY() - northWestCorner.getY();
        if (yDiff != 0)
            return -1;
        if (zDiff != xDiff)
            return -1;
        return zDiff + 1;
    }

    public static BlockPos findLowestPoint(World world, BlockPos startPoint, Predicate<BlockState> isSuitable) {
        while (true) {
            if (!isSuitable.test(world.getBlockState(startPoint.below())))
                return startPoint;
            startPoint = startPoint.below();
        }
    }

    public static Pair<BlockPos, BlockPos> findCorners(World world, BlockPos lowestPoint, Predicate<BlockState> isSuitable) {
        BlockPos northWestCorner = lowestPoint;
        while (isSuitable.test(world.getBlockState(northWestCorner.north()))) {
            northWestCorner = northWestCorner.north();
        }
        while (isSuitable.test(world.getBlockState(northWestCorner.west()))) {
            northWestCorner = northWestCorner.west();
        }
        BlockPos southEastCorner = lowestPoint;
        while (isSuitable.test(world.getBlockState(northWestCorner.south()))) {
            southEastCorner = southEastCorner.south();
        }
        while (isSuitable.test(world.getBlockState(northWestCorner.east()))) {
            southEastCorner = southEastCorner.east();
        }
        return Pair.of(northWestCorner, southEastCorner);
    }

    public static void handlePartDestroy(World world, BlockPos pos, int maxSize, Predicate<BlockState> controllerPredicate) {
        Pair<BlockPos, BlockState> controllerData = ScanUtils.findBlockInRadius(world, pos, maxSize + 1, controllerPredicate);
        if (controllerData == null) {
            ProgressivePeripherals.LOGGER.error(String.format("Cannot correctly destroy %s, ignoring", pos));
            return;
        }
        TileEntity entity = world.getBlockEntity(controllerData.getLeft());
        if (!(entity instanceof IMultiBlockController))
            throw new IllegalArgumentException("Incorrect multiblock controller");
        ((IMultiBlockController) entity).deconstructMultiBlock();
    }
}
