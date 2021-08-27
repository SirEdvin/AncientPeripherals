package site.siredvin.progressiveperipherals.integrations.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.item.crafting.IRecipeType;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        // transformer

        RecipeRegistryToolkit.registerRecipeSerializerRaw(ProcessingRecipe.class, new RecipeTransformer<ProcessingRecipe<?>>() {
            @Override
            public List<?> getInputs(ProcessingRecipe<?> recipe) {
                List<Object> ingredients = new ArrayList<>();
                ingredients.addAll(recipe.getIngredients());
                ingredients.addAll(recipe.getFluidIngredients());
                return ingredients;
            }

            @Override
            public List<?> getOutputs(ProcessingRecipe<?> recipe) {
                List<Object> results = new ArrayList<>();
                results.addAll(recipe.getRollableResults());
                results.addAll(recipe.getFluidResults());
                return results;
            }

            @Override
            public Map<String, Object> getExtraData(ProcessingRecipe<?> recipe) {
                return new HashMap<String, Object>() {{
                    put("processingDuration", recipe.getProcessingDuration());
                    put("heat", recipe.getRequiredHeat().serialize());
                }};
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(ProcessingOutput.class, processingOutput -> new HashMap<String, Object>(){{
                put("item", RecipeRegistryToolkit.serialize(processingOutput.getStack()));
                put("chance", processingOutput.getChance());
        }});

        RecipeRegistryToolkit.registerSerializer(FluidIngredient.class, fluidIngredient -> RecipeRegistryToolkit.GSON.fromJson(fluidIngredient.serialize(), HashMap.class));

        // searcher

        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<BasinRecipe>)AllRecipeTypes.BASIN.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<BasinRecipe>)AllRecipeTypes.MIXING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
    }
}
