package site.siredvin.progressiveperipherals.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IForgeShearable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RepresentationUtil {

    public static <T> String mergeValues(Collection<T> objects) {
        StringBuilder builder = new StringBuilder();
        for (T obj: objects) {
            builder.append(obj.toString());
            builder.append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }

    public static Map<String, Object> entityToLua(Entity entity) {
        Map<String, Object> data = new HashMap<>();
        data.put("entityID", entity.getId());
        data.put("entityUUID", entity.getStringUUID());
        data.put("name", entity.getName().getString());
        data.put("tags", entity.getTags());
        return data;
    }

    public static Map<String, Object> animalToLua(AnimalEntity animal, ItemStack itemInHand) {
        Map<String, Object> data = entityToLua(animal);
        data.put("baby", animal.isBaby());
        data.put("inLove", animal.isInLove());
        data.put("aggressive", animal.isAggressive());
        if (animal instanceof IForgeShearable && !itemInHand.isEmpty()) {
            IForgeShearable shareable = (IForgeShearable)animal;
            data.put("shareable", shareable.isShearable(itemInHand, animal.level, animal.blockPosition()));
        }

        return data;
    }

    public static Map<String, Object> completeEntityToLua(Entity entity, ItemStack itemInHand) {
        return entity instanceof AnimalEntity ? animalToLua((AnimalEntity)entity, itemInHand) : entityToLua(entity);
    }

    public static Map<String, Object> completeEntityWithPositionToLua(Entity entity, ItemStack itemInHand, BlockPos pos) {
        Map<String, Object> data = completeEntityToLua(entity, itemInHand);
        data.put("x", entity.getX() - (double)pos.getX());
        data.put("y", entity.getY() - (double)pos.getY());
        data.put("z", entity.getZ() - (double)pos.getZ());
        return data;
    }
}
