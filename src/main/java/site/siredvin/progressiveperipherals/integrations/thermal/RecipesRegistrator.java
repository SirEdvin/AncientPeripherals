package site.siredvin.progressiveperipherals.integrations.thermal;

import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.recipes.device.RockGenMapping;
import cofh.thermal.core.util.recipes.device.TreeExtractorMapping;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import net.minecraft.item.ItemStack;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.*;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {
    @Override
    public void run() {
        RecipeRegistryToolkit.registerRecipeSerializer(RockGenMapping.class, new RecipeTransformer<RockGenMapping>() {
            @Override
            public List<?> getInputs(RockGenMapping recipe) {
                return new ArrayList<Object>() {{
                    add(new ItemStack(recipe.getAdjacent().asItem()));
                    add(new ItemStack(recipe.getBelow().asItem()));
                }};
            }

            @Override
            public List<?> getOutputs(RockGenMapping recipe) {
                return Collections.singletonList(recipe.getResult());
            }

            @Override
            public Map<String, Object> getExtraData(RockGenMapping recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTime());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(TreeExtractorMapping.class, new RecipeTransformer<TreeExtractorMapping>() {
            @Override
            public List<?> getInputs(TreeExtractorMapping recipe) {
                return new ArrayList<Object>() {{
                    add(new ItemStack(recipe.getTrunk().asItem()));
                    add(new ItemStack(recipe.getLeaves().asItem()));
                }};
            }

            @Override
            public List<?> getOutputs(TreeExtractorMapping recipe) {
                return Collections.singletonList(recipe.getFluid());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ThermalRecipe.class, new RecipeTransformer<ThermalRecipe>() {
            @Override
            public List<?> getInputs(ThermalRecipe recipe) {
                List<Object> data = new ArrayList<>();
                data.addAll(recipe.getInputItems());
                data.addAll(recipe.getInputFluids());
                return data;
            }

            @Override
            public List<?> getOutputs(ThermalRecipe recipe) {
                List<Object> data = new ArrayList<>();
                data.addAll(recipe.getOutputItems());
                data.addAll(recipe.getOutputFluids());
                return data;
            }

            @Override
            public Map<String, Object> getExtraData(ThermalRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("outputChances", LuaUtils.toLua(recipe.getOutputItemChances()));
                    put("energy", recipe.getEnergy());
                    put("xp", recipe.getXp());
                    put("catalyzable", recipe.isCatalyzable());
                }};
            }
        });

        // predicates

        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_FURNACE,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_SAWMILL,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_PULVERIZER,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_PULVERIZER_RECYCLE,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_SMELTER,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_SMELTER_RECYCLE,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_INSOLATOR,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_CENTRIFUGE,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_PRESS,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_CRUCIBLE,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_CHILLER,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_REFINERY,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_PYROLYZER,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_BOTTLER,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
        RecipeRegistryToolkit.registerRecipePredicate(TCoreRecipeTypes.RECIPE_BREWER,  RecipeSearchUtils.buildPredicate(ThermalRecipe::getOutputItems));
    }
}
