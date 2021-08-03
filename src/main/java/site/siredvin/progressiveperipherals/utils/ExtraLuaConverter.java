package site.siredvin.progressiveperipherals.utils;

import de.srendi.advancedperipherals.common.util.LuaConverter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.FakePlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtraLuaConverter {

    public static Map<String, Object>  classifyEntity(Entity entity) {
        Map<String, Object> baseData = LuaConverter.entityToLua(entity);
        Set<String> tags = (Set<String>) baseData.get("tags");
        Set<String> newTags = new HashSet<>(tags);
        if (entity instanceof PlayerEntity)
            newTags.add("progressiveperipherals/player");
        if (entity instanceof FakePlayer)
            newTags.add("progressiveperipherals/fake_player");
        if (entity instanceof AnimalEntity)
            newTags.add("progressiveperipherals/animal");
        if (entity instanceof ItemEntity)
            newTags.add("progressiveperipherals/item");
        if (entity instanceof ExperienceOrbEntity)
            newTags.add("progressiveperipherals/xp_orb");
        baseData.put("tags", newTags);
        return baseData;
    }
}
