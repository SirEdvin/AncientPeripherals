package site.siredvin.progressiveperipherals.integrations.tconstruct;

import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;
import site.siredvin.progressiveperipherals.utils.LuaUtils;
import slimeknights.mantle.recipe.FluidIngredient;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;

import java.util.*;

public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        RecipeRegistryToolkit.registerRecipeSerializer(MeltingRecipe.class, new RecipeTransformer<MeltingRecipe>() {
            @Override
            public List<?> getInputs(MeltingRecipe recipe) {
                return recipe.getIngredients();
            }

            @Override
            public List<?> getOutputs(MeltingRecipe recipe) {
                List<Object> data = new ArrayList<>();
                recipe.getOutputWithByproducts().forEach(data::addAll);
                return data;
            }

            @Override
            public Map<String, Object> getExtraData(MeltingRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTime());
                    put("temperature", recipe.getTemperature());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(EntityMeltingRecipe.class, new RecipeTransformer<EntityMeltingRecipe>() {
            @Override
            public List<?> getInputs(EntityMeltingRecipe recipe) {
                List<Object> data = new ArrayList<>();
                recipe.getDisplayInputs().forEach(data::addAll);
                return data;
            }

            @Override
            public List<?> getOutputs(EntityMeltingRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(AlloyRecipe.class, new RecipeTransformer<AlloyRecipe>() {
            @Override
            public List<?> getInputs(AlloyRecipe recipe) {
                List<Object> data = new ArrayList<>();
                recipe.getDisplayInputs().forEach(data::addAll);
                return data;
            }

            @Override

            public List<?> getOutputs(AlloyRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemCastingRecipe.class, new RecipeTransformer<ItemCastingRecipe>() {
            @Override
            public List<?> getInputs(ItemCastingRecipe recipe) {
                return Collections.singletonList(recipe.getFluid());
            }

            @Override
            public List<?> getOutputs(ItemCastingRecipe recipe) {
                return Collections.singletonList(recipe.getResultItem());
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(FluidIngredient.class, fluidIngredient -> new HashMap<String, Object>(){{
            put("variants", LuaUtils.toLua(fluidIngredient.getFluids().stream().map(RecipeRegistryToolkit::serialize)));
        }});
    }
}
