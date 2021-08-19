package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.shared.util.NBTUtil;
import de.srendi.advancedperipherals.common.util.LuaConverter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.FakePlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtraLuaConverter {

    @SuppressWarnings("unchecked")
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

    public static Map<String, Object> shortStackInfo(ItemStack stack) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", stack.getCount());
        data.put("name", stack.getItem().getRegistryName().toString());
        return data;
    }

    public static Map<String, Object> stackInfo(ItemStack stack, boolean expandNBT) {
        return stackInfo(stack, stack.getMaxStackSize(), expandNBT);
    }

    public static Map<String, Object> stackInfo(ItemStack stack, int maxStackSize, boolean expandNBT) {
        Map<String, Object> data = shortStackInfo(stack);
        data.put("displayName", stack.getDisplayName().getString());
        CompoundNBT tag = stack.getTag();
        if (tag != null && !tag.isEmpty()) {
            if (!expandNBT) {
                data.put("nbt", NBTUtil.getNBTHash(tag));
            } else {
                data.put("nbt", NBTUtil.toLua(tag));
            }
        }
        data.put("tags", LuaConverter.tagsToList(stack.getItem().getTags()));
        data.put("maxCount", maxStackSize);
        return data;
    }
}
