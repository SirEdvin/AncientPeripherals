package site.siredvin.progressiveperipherals.utils.dao;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.io.Serializable;
import java.util.List;

public class QuadList implements Serializable {
    public List<QuadData> list;

    public QuadList(List<QuadData> list){
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuadList))
            return false;
        return list.equals(((QuadList) o).list);
    }

    public VoxelShape shape() {
        return list.stream().map(QuadData::shape).reduce(VoxelShapes::or).orElse(VoxelShapes.empty());
    }
}
