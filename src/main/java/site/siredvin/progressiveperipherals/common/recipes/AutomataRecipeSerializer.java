package site.siredvin.progressiveperipherals.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AutomataRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AutomataRecipe> {

    private final IRecipeType<AutomataRecipe> recipeType;

    public AutomataRecipeSerializer() {
        recipeType = IRecipeType.register(AutomataRecipe.GROUP);
    }

    @Override
    public @NotNull AutomataRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject recipeData) {
        Map<String, Ingredient> map = AutomataRecipe.keyFromJson(JSONUtils.getAsJsonObject(recipeData, "key"));
        String[] patterns = AutomataRecipe.patternFromJson(JSONUtils.getAsJsonArray(recipeData, "pattern"));
        NonNullList<Ingredient> ingredients = AutomataRecipe.dissolvePattern(patterns, map);
        ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(recipeData, "result"));
        return new AutomataRecipe(id, ingredients, itemstack);
    }

    @Nullable
    @Override
    public AutomataRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull PacketBuffer buffer) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(AutomataRecipe.MAX_HEIGHT * AutomataRecipe.MAX_WIDTH, Ingredient.EMPTY);

        for (int k = 0; k < ingredients.size(); k++) {
            ingredients.set(k, Ingredient.fromNetwork(buffer));
        }

        ItemStack itemstack = buffer.readItem();

        return new AutomataRecipe(id, ingredients, itemstack);
    }

    @Override
    public void toNetwork(@NotNull PacketBuffer buffer, AutomataRecipe recipe) {
        for(Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem());
    }

    public IRecipeType<AutomataRecipe> getRecipeType() {
        return recipeType;
    }
}
