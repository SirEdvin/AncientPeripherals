package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class RecipeTransformer<T extends IRecipe<? extends IInventory>> {

    public abstract List<?> getInputs(T recipe);

    public abstract List<?> getOutputs(T recipe);

    public Map<String, Object> transform(T recipe) {
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("id", recipe.getId().toString());
        recipeData.put("output", getOutputs(recipe).stream().map(RecipeRegistryToolkit::serialize).collect(Collectors.toList()));
        recipeData.put("inputs", getInputs(recipe).stream().map(RecipeRegistryToolkit::serialize).collect(Collectors.toList()));
        return recipeData;
    }
}
