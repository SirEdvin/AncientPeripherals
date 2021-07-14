package site.siredvin.ancientperipherals.utils;

import net.minecraft.util.math.BlockPos;

public class PositionUtils {
    public static boolean radiusCorrect(BlockPos first, BlockPos second, int radius) {
        if (Math.abs(first.getX() - second.getX()) > radius)
            return false;
        if (Math.abs(first.getY() - second.getY()) > radius)
            return false;
        return Math.abs(first.getZ() - second.getZ()) <= radius;
    }
}
