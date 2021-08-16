package site.siredvin.progressiveperipherals.utils.quad;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuadList implements Serializable {
    public final List<QuadData> list;

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

    public Map<Integer, Map<String, Object>> toLua() {
        Map<Integer, Map<String, Object>> data = new HashMap<>();
        for (int i = 0; i< list.size(); i++) {
            data.put(i + 1, list.get(i).toLua());
        }
        return data;
    }

    @Override
    public String toString() {
        return "QuadList{" +
                "list=" + list +
                '}';
    }
}
