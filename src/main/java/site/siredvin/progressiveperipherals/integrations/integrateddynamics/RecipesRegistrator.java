package site.siredvin.progressiveperipherals.integrations.integrateddynamics;

import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeDryingBasin;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {
    @Override
    public void run() {
        // transforms
        RecipeRegistryToolkit.registerRecipeSerializer(RecipeDryingBasin.class, new RecipeTransformer<RecipeDryingBasin>() {
            @Override
            public List<?> getInputs(RecipeDryingBasin recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getInputIngredient());
                    add(recipe.getInputFluid());
                }};
            }

            @Override
            public List<?> getOutputs(RecipeDryingBasin recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getRecipeOutput());
                    add(recipe.getOutputFluid());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeSqueezer.class, new RecipeTransformer<RecipeSqueezer>() {
            @Override
            public List<?> getInputs(RecipeSqueezer recipe) {
                return Collections.singletonList(recipe.getInputIngredient());
            }

            @Override
            public List<?> getOutputs(RecipeSqueezer recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getOutputItems());
                    add(recipe.getOutputFluid());
                }};
            }
        });

        // predicates
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_DRYING_BASIN, RecipeSearchUtils.buildPredicate(recipe -> Arrays.asList(recipe.getInputIngredient().getItems())));
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN, RecipeSearchUtils.buildPredicate(recipe -> Arrays.asList(recipe.getInputIngredient().getItems())));
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_SQUEEZER, RecipeSearchUtils.buildPredicate(recipe -> Arrays.asList(recipe.getInputIngredient().getItems())));
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER, RecipeSearchUtils.buildPredicate(recipe -> Arrays.asList(recipe.getInputIngredient().getItems())));
    }
}
