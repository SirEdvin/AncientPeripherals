package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.utils.quad.QuadData;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LuaUtils {
    private static final int MAX_QUAD_VECTOR = 48;

    private static Vector3f buildVector(float x, float y, float z, float min, float max) throws LuaException {
        if (x < min || y < min || z < min)
            throw new LuaException(String.format("Coordinate lower then %.2f", min));
        if (x > max || y > max || z > max)
            throw new LuaException(String.format("Coordinate bigger then %.2f", min));
        return new Vector3f(x, y, z);
    }

    // BlockPos tricks

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

    // Quad tricks

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
        return convertToColor(obj, 255);
    }

    public static Color convertToColor(@Nullable Object obj, int colorAlpha) throws LuaException {
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
        return new Color(((Number) red).intValue(), ((Number) green).intValue(), ((Number) blue).intValue(), colorAlpha);
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

    // Array tricks

    public static Map<Integer, Double> toLua(double[] value) {
        Map<Integer, Double> data = new HashMap<>();
        for (int i = 0;i < value.length; i++) {
            data.put(i + 1, value[i]);
        }
        return data;
    }

    public static Map<Integer, Float> toLua(float[] value) {
        Map<Integer, Float> data = new HashMap<>();
        for (int i = 0;i < value.length; i++) {
            data.put(i + 1, value[i]);
        }
        return data;
    }

    public static Map<Integer, Integer> toLua(int[] value) {
        Map<Integer, Integer> data = new HashMap<>();
        for (int i = 0;i < value.length; i++) {
            data.put(i + 1, value[i]);
        }
        return data;
    }

    public static <T, V> Map<Integer, V> toLua(T[] value, Function<T, V> mapper) {
        Map<Integer, V> data = new HashMap<>();
        for (int i = 0;i < value.length; i++) {
            data.put(i + 1, mapper.apply(value[i]));
        }
        return data;
    }

    public static <T> Map<Integer, T> toLua(List<T> value) {
        Map<Integer, T> data = new HashMap<>();
        for (int i = 0; i < value.size(); i++) {
            data.put(i + 1, value.get(i));
        }
        return data;
    }

    public static <T> Map<Integer, T> toLua(Stream<T> value) {
        Counter counter = new Counter();
        return value.collect(Collectors.toMap(el -> counter.getAndIncrease(), el -> el));
    }

    public static Map<Integer, Map<Integer, Double>> toLua(double[][] value) {
        Map<Integer, Map<Integer, Double>> data = new HashMap<>();
        for (int i = 0;i < value.length; i++) {
            data.put(i + 1, toLua(value[i]));
        }
        return data;
    }

    public static Map<String, Integer> toLua(Color color) {
        return new HashMap<String, Integer>() {{
           put("red", color.getRed());
           put("green", color.getGreen());
           put("blue", color.getBlue());
        }};
    }

    public static double[] toArray(Map<Double, Number> data) throws LuaException {
        int size = data.keySet().size();
        double[] value = new double[size];
        for (int i = 0; i < size; i++) {
            Number valueCandidate = data.get((double) (i + 1));
            if (valueCandidate == null)
                throw new LuaException(String.format("Cannot find element for number %d", i + 1));
            value[i] = valueCandidate.doubleValue();
        }
        return value;
    }

    public static double[][] toArrayOfArrays(Map<Double, Map<Double, Number>> data) throws LuaException {
        int size = data.keySet().size();
        double[][] value = new double[size][];
        for (int i = 0; i < size; i++) {
            Map<Double, Number> valueCandidate = data.get((double) (i + 1));
            if (valueCandidate == null)
                throw new LuaException(String.format("Cannot find element for number %d", i + 1));
            value[i] = toArray(valueCandidate);
        }
        return value;
    }

    // argument tricks

    public static ResourceLocation getResourceLocation(@NotNull IArguments arguments, int index) throws LuaException {
        return toResourceLocation(arguments.getString(index));
    }

    public static ResourceLocation toResourceLocation(String value) throws LuaException {
        try{
            return new ResourceLocation(value);
        } catch (ResourceLocationException e) {
            throw new LuaException(e.getMessage());
        }
    }

}
