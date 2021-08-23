package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.api.lua.LuaException;
import net.minecraft.util.math.BlockPos;

public class CheckUtils {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean radiusCorrect(BlockPos first, BlockPos second, int radius) {
        if (Math.abs(first.getX() - second.getX()) > radius)
            return false;
        if (Math.abs(first.getY() - second.getY()) > radius)
            return false;
        return Math.abs(first.getZ() - second.getZ()) <= radius;
    }

    public static void isCorrectSlot(int slot) throws LuaException {
        isCorrectSlot(slot, "target");
    }

    public static void isCorrectSlot(int slot, String name) throws LuaException {
        if (slot < 1 || slot > 16)
            throw new LuaException(String.format("%s slot is incorrectly defined", name));
    }
}
