package site.siredvin.progressiveperipherals.utils.dao;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3f;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QuadData implements Serializable {
    public final float x1;
    public final float y1;
    public final float z1;
    public final float x2;
    public final float y2;
    public final float z2;
    public final Color color;

    public QuadData(float x1, float y1, float z1, float x2, float y2, float z2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        // For some strange reason, colors order in minecraft vertex are swapper
        // and it not RGB, but BGR
        this.color = new Color(color.getBlue(), color.getGreen(), color.getRed());
    }

    public QuadData(Vector3f start, Vector3f end, Color color) {
        this(start.x(), start.y(), start.z(), end.x(), end.y(), end.z(), color);
    }

    public Vector3f getStart() {
        return new Vector3f(x1, y1, z1);

    }

    public Vector3f getEnd() {
        return new Vector3f(x2, y2, z2);
    }

    public int getColor() {
        return color.getRGB();
    }

    public float[] getUV() {
        // TODO: side-depends?
        return new float[]{x1, z1, x2, z2};
    }

    public VoxelShape shape() {
        return VoxelShapes.box(x1 / 16, y1 / 16, z1 / 16, x2 / 16, y2 / 16, z2 / 16);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuadData))
            return false;
        QuadData other = (QuadData) o;
        return x1 == other.x1 && x2 == other.x2 && y1 == other.y1 && y2 == other.y2 && z1 == other.z1 && z2 == other.z2 && color.equals(other.color);
    }

    public Map<String, Object> toLua() {
        Map<String, Object> data = new HashMap<>();
        data.put("x1", x1);
        data.put("y1", y1);
        data.put("z1", z1);
        data.put("x2", x2);
        data.put("y2", y2);
        data.put("z2", z2);
        data.put("color", color.toString());
        data.put("colorRGB", color.getRGB());
        return data;
    }
}
