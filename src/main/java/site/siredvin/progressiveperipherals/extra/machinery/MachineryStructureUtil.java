package site.siredvin.progressiveperipherals.extra.machinery;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.utils.ScanUtils;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MachineryStructureUtil {
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

    public static @Nullable BlockPos findLowestPoint(World world, BlockPos startPoint, Predicate<BlockState> isSuitable, int deep) {
        for (int i = 0; i < deep; i++) {
            if (!isSuitable.test(world.getBlockState(startPoint.below())))
                break;
            startPoint = startPoint.below();
        }
        if (isSuitable.test(world.getBlockState(startPoint)))
            return startPoint;
        return null;
    }

    public static @Nullable Pair<BlockPos, BlockPos> findCorners(World world, @NotNull BlockPos lowestPoint, Predicate<BlockState> isSuitable, int deep) {
        BlockPos northWestCorner = lowestPoint;
        for (int i = 0; i < deep; i++) {
            if (!isSuitable.test(world.getBlockState(northWestCorner.north())))
                break;
            northWestCorner = northWestCorner.north();
        }
        for (int i = 0; i < deep; i++) {
            if (!isSuitable.test(world.getBlockState(northWestCorner.west())))
                break;
            northWestCorner = northWestCorner.west();
        }
        BlockPos southEastCorner = lowestPoint;
        for (int i = 0; i < deep; i++) {
            if (!isSuitable.test(world.getBlockState(southEastCorner.south())))
                break;
            southEastCorner = southEastCorner.south();
        }
        for (int i = 0; i < deep; i++) {
            if (!isSuitable.test(world.getBlockState(southEastCorner.east())))
                break;
            southEastCorner = southEastCorner.east();
        }
        if (!isSuitable.test(world.getBlockState(northWestCorner)))
            return null;
        if (!isSuitable.test(world.getBlockState(southEastCorner)))
            return null;
        return Pair.of(northWestCorner, southEastCorner);
    }

    public static void handlePartDestroy(World world, BlockPos pos, int maxSize, BiPredicate<BlockState, BlockPos> controllerPredicate) {
        Pair<BlockPos, BlockState> controllerData = ScanUtils.findBlockInRadius(world, pos, maxSize + 1, controllerPredicate);
        if (controllerData == null) {
            ProgressivePeripherals.LOGGER.error(String.format("Cannot correctly destroy %s, ignoring", pos));
            return;
        }
        TileEntity entity = world.getBlockEntity(controllerData.getLeft());
        if (!(entity instanceof IMachineryController))
            throw new IllegalArgumentException("Incorrect multiblock controller");
        ((IMachineryController<?>) entity).deconstructMultiBlock();
    }
}
