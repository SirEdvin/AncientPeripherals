package site.siredvin.ancientperipherals.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public class AutomataRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AutomataRecipe> {

    @Override
    public AutomataRecipe fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_) {
        String s = JSONUtils.getAsString(p_199425_2_, "group", "");
        Map<String, Ingredient> map = AutomataRecipe.keyFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "key"));
        String[] patterns = AutomataRecipe.patternFromJson(JSONUtils.getAsJsonArray(p_199425_2_, "pattern"));
        NonNullList<Ingredient> nonnulllist = AutomataRecipe.dissolvePattern(patterns, map);
        ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "result"));
        return new AutomataRecipe(p_199425_1_, s, nonnulllist, itemstack);
    }

    @Nullable
    @Override
    public AutomataRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
        String s = p_199426_2_.readUtf(32767);
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int k = 0; k < nonnulllist.size(); ++k) {
            nonnulllist.add(Ingredient.fromNetwork(p_199426_2_));
        }

        ItemStack itemstack = p_199426_2_.readItem();
        return new AutomataRecipe(p_199426_1_, s, nonnulllist, itemstack);
    }

    @Override
    public void toNetwork(PacketBuffer p_199427_1_, AutomataRecipe p_199427_2_) {
        p_199427_1_.writeUtf(p_199427_2_.getGroup());

        for(Ingredient ingredient : p_199427_2_.getIngredients()) {
            ingredient.toNetwork(p_199427_1_);
        }

        p_199427_1_.writeItem(p_199427_2_.getResultItem());
    }
}
