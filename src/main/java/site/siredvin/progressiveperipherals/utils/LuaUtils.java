package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.api.lua.LuaException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.utils.quad.QuadData;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LuaUtils {
    public static final int MAX_QUAD_VECTOR = 48;

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

    public static Vector3f convertToStartVector(Map<?, ?> table, float min, float max) throws LuaException {
        if (!table.containsKey("x1") || !table.containsKey("y1") || !table.containsKey("z1"))
            throw new LuaException("Table should have start coordinates");
        Object x = table.get("x1");
        Object y = table.get("y1");
        Object z = table.get("z1");
        if (!(x instanceof Number) || !(y instanceof Number) || !(z instanceof Number))
            throw new LuaException("Table should have start coordinates");
        return buildVector(((Number) x).floatValue(), ((Number) y).floatValue(), ((Number) z).floatValue(), min, max);
    }

    public static Vector3f convertToEndVector(Map<?, ?> table, float min, float max) throws LuaException {
        if (!table.containsKey("x2") || !table.containsKey("y2") || !table.containsKey("z2"))
            throw new LuaException("Table should have end coordinates");
        Object x = table.get("x2");
        Object y = table.get("y2");
        Object z = table.get("z2");
        if (!(x instanceof Number) || !(y instanceof Number) || !(z instanceof Number))
            throw new LuaException("Table should have end coordinates");
        return buildVector(((Number) x).floatValue(), ((Number) y).floatValue(), ((Number) z).floatValue(), min, max);
    }

    public static Color convertToColor(@Nullable Object obj) throws LuaException {
        if (obj == null)
            return Color.WHITE;
        if (!(obj instanceof Map))
            throw new LuaException("Color should be table");
        Map<?, ?> table = (Map<?, ?>) obj;
        if (!table.containsKey("red") || !table.containsKey("green") || !table.containsKey("blue"))
            throw new LuaException("Table should have color RGB codes");
        Object red = table.get("red");
        Object green = table.get("green");
        Object blue = table.get("blue");
        if (!(red instanceof Number) || !(green instanceof Number) || !(blue instanceof Number))
            throw new LuaException("Table should have color RGB codes");
        return new Color(((Number) red).intValue(), ((Number) green).intValue(), ((Number) blue).intValue());
    }

    public static QuadData convertToQuadData(Map<?, ?> table) throws LuaException {
        return new QuadData(
                convertToStartVector(table, 0, MAX_QUAD_VECTOR),
                convertToEndVector(table, 0, MAX_QUAD_VECTOR),
                convertToColor(table.get("color"))
        );
    }

    public static QuadList convertToQuadList(Map<?, ?> table) throws LuaException {
        List<QuadData> data = new ArrayList<>();
        for (Object value: table.values()) {
            if (!(value instanceof Map))
                throw new LuaException("Table should be quad list");
            data.add(convertToQuadData((Map<?, ?>) value));
        }
        return new QuadList(data);
    }

    private static Vector3f buildVector(float x, float y, float z, float min, float max) throws LuaException {
        if (x < min || y < min || z < min)
            throw new LuaException(String.format("Coordinate lower then %.2f", min));
        if (x > max || y > max || z > max)
            throw new LuaException(String.format("Coordinate bigger then %.2f", min));
        return new Vector3f(x, y, z);
    }
}
