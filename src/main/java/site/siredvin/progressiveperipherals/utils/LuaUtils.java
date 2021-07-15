package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.api.lua.LuaException;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class LuaUtils {
    public static BlockPos convertToBlockPos(Map<?, ?> table) throws LuaException {
        if (!table.containsKey("x") || !table.containsKey("y") || !table.containsKey("z"))
            throw new LuaException("Table should be block position table");
        Object x = table.get("x");
        Object y = table.get("y");
        Object z = table.get("z");
        if (!(x instanceof Number) || !(y instanceof Number) || !(z instanceof Number))
            throw new LuaException("Table should be block position table");
        return new BlockPos(((Number) x).intValue(), ((Number) y).intValue(), ((Number) z).intValue());
    }

    public static BlockPos convertToBlockPos(BlockPos center, Map<?, ?> table) throws LuaException {
        BlockPos relative = convertToBlockPos(table);
        return new BlockPos(center.getX() + relative.getX(), center.getY() + relative.getY(), center.getZ() + relative.getZ());
    }
}
