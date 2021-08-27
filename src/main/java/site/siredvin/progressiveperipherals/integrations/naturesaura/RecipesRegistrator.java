package site.siredvin.progressiveperipherals.integrations.naturesaura;

import de.ellpeck.naturesaura.recipes.*;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        // transformers
        RecipeRegistryToolkit.registerRecipeSerializer(AltarRecipe.class, new RecipeTransformer<AltarRecipe>() {
            @Override
            public List<?> getInputs(AltarRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.input);
                    add(recipe.catalyst);
                    add(recipe.getDimensionBottle());
                }};
            }

            @Override
            public List<?> getOutputs(AltarRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(AltarRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                    put("aura", recipe.aura);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(AnimalSpawnerRecipe.class, new RecipeTransformer<AnimalSpawnerRecipe>() {
            @Override
            public List<?> getInputs(AnimalSpawnerRecipe recipe) {
                return Arrays.asList(recipe.ingredients);
            }

            @Override
            public List<?> getOutputs(AnimalSpawnerRecipe recipe) {
                return Collections.singletonList(recipe.entity);
            }

            @Override
            public Map<String, Object> getExtraData(AnimalSpawnerRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                    put("aura", recipe.aura);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(OfferingRecipe.class, new RecipeTransformer<OfferingRecipe>() {
            @Override
            public List<?> getInputs(OfferingRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.input);
                    add(recipe.startItem);
                }};
            }

            @Override
            public List<?> getOutputs(OfferingRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(TreeRitualRecipe.class, new RecipeTransformer<TreeRitualRecipe>() {
            @Override
            public List<?> getInputs(TreeRitualRecipe recipe) {
                List<Object> data = new ArrayList<>();
                Collections.addAll(data, recipe.ingredients);
                data.add(recipe.saplingType);
                return data;
            }

            @Override
            public List<?> getOutputs(TreeRitualRecipe recipe) {
                return Collections.singletonList(recipe.result);
            }

            @Override
            public Map<String, Object> getExtraData(TreeRitualRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                }};
            }
        });

        // searchers

        RecipeRegistryToolkit.registerRecipePredicate(ModRecipes.ALTAR_TYPE, RecipeSearchUtils.buildPredicateSingle(altarRecipe -> altarRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(ModRecipes.OFFERING_TYPE, RecipeSearchUtils.buildPredicateSingle(offeringRecipe -> offeringRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(ModRecipes.TREE_RITUAL_TYPE, RecipeSearchUtils.buildPredicateSingle(treeRitualRecipe -> treeRitualRecipe.result));
    }
}
