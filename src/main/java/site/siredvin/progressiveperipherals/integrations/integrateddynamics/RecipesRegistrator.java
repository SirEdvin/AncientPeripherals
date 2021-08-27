package site.siredvin.progressiveperipherals.integrations.integrateddynamics;

import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeDryingBasin;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;
import java.util.stream.Collectors;

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
                    add(recipe.getOutputItem());
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
                    addAll(recipe.getOutputItems());
                    add(recipe.getOutputFluid());
                }};
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(RecipeSqueezer.ItemStackChance.class, itemStackChance -> new HashMap<String, Object>() {{
            put("item", RecipeRegistryToolkit.serialize(itemStackChance.getItemStack()));
            put("chance", itemStackChance.getChance());
        }});

        // predicates
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_DRYING_BASIN, RecipeSearchUtils.buildPredicateSingle(RecipeDryingBasin::getOutputItem));
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN, RecipeSearchUtils.buildPredicateSingle(RecipeDryingBasin::getOutputItem));
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_SQUEEZER,
                RecipeSearchUtils.buildPredicate(recipe -> recipe.getOutputItems().stream().map(RecipeSqueezer.ItemStackChance::getItemStack).collect(Collectors.toList())));
        RecipeRegistryToolkit.registerRecipePredicate(RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER,
                RecipeSearchUtils.buildPredicate(recipe -> recipe.getOutputItems().stream().map(RecipeSqueezer.ItemStackChance::getItemStack).collect(Collectors.toList())));
    }
}
