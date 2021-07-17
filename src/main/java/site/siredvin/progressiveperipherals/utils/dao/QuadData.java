package site.siredvin.progressiveperipherals.utils.dao;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3f;

import java.io.Serializable;

public class QuadData implements Serializable {
    private transient Vector3f start;
    private transient Vector3f end;
    private transient float[] UV;

    public final float x1;
    public final float y1;
    public final float z1;
    public final float x2;
    public final float y2;
    public final float z2;

    public QuadData(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public QuadData(Vector3f start, Vector3f end) {
        this(start.x(), start.y(), start.z(), end.x(), end.y(), end.z());
        this.start = start;
        this.end = end;
    }

    public Vector3f getStart() {
        if (start == null)
            start = new Vector3f(x1, y1, z1);
        return start;
    }

    public Vector3f getEnd() {
        if (end == null)
            end = new Vector3f(x2, y2, z2);
        return end;
    }

    public float[] getUV() {
        if (UV == null)
            UV = new float[]{x1, z1, x2, z2};
        return UV;
    }

    public VoxelShape shape() {
        return VoxelShapes.box(x1 / 16, y1 / 16, z1 / 16, x2 / 16, y2 / 16, z2 / 16);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuadData))
            return false;
        QuadData other = (QuadData) o;
        return x1 == other.x1 && x2 == other.x2 && y1 == other.y1 && y2 == other.y2 && z1 == other.z1 && z2 == other.z2;
    }
}
