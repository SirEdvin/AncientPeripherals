package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.item.crafting.IRecipe;

import java.util.Collections;
import java.util.List;

public class DefaultRecipeTransformer extends RecipeTransformer<IRecipe<?>> {

    @Override
    public List<?> getInputs(IRecipe<?> recipe) {
        return recipe.getIngredients();
    }

    @Override
    public List<?> getOutputs(IRecipe<?> recipe) {
        return Collections.singletonList(recipe.getResultItem());
    }
}
