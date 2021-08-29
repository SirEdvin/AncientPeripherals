package site.siredvin.progressiveperipherals.integrations.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.content.contraptions.components.crusher.CrushingRecipe;
import com.simibubi.create.content.contraptions.components.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.contraptions.components.fan.SplashingRecipe;
import com.simibubi.create.content.contraptions.components.millstone.MillingRecipe;
import com.simibubi.create.content.contraptions.components.mixer.CompactingRecipe;
import com.simibubi.create.content.contraptions.components.mixer.MixingRecipe;
import com.simibubi.create.content.contraptions.components.press.PressingRecipe;
import com.simibubi.create.content.contraptions.components.saw.CuttingRecipe;
import com.simibubi.create.content.contraptions.fluids.actors.FillingRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedRecipe;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.content.contraptions.processing.EmptyingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.curiosities.tools.SandPaperPolishingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.item.crafting.IRecipeType;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    @SuppressWarnings("unchecked")
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

        RecipeRegistryToolkit.registerRecipeSerializer(SequencedAssemblyRecipe.class, new RecipeTransformer<SequencedAssemblyRecipe>() {
            @Override
            public List<?> getInputs(SequencedAssemblyRecipe recipe) {
                return recipe.getSequence();
            }

            @Override
            public List<?> getOutputs(SequencedAssemblyRecipe recipe) {
                return Collections.singletonList(recipe.getResultItem());
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(ProcessingOutput.class, processingOutput -> new HashMap<String, Object>(){{
                put("item", RecipeRegistryToolkit.serialize(processingOutput.getStack()));
                put("chance", processingOutput.getChance());
        }});

        RecipeRegistryToolkit.registerSerializer(FluidIngredient.class, fluidIngredient -> RecipeRegistryToolkit.GSON.fromJson(fluidIngredient.serialize(), HashMap.class));
        RecipeRegistryToolkit.registerSerializer(SequencedRecipe.class, sequencedRecipe -> RecipeRegistryToolkit.serializeRecipe(sequencedRecipe.getRecipe()));

        // searcher

        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<ConversionRecipe>)AllRecipeTypes.CONVERSION.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<CrushingRecipe>)AllRecipeTypes.CRUSHING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<CuttingRecipe>)AllRecipeTypes.CUTTING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<MillingRecipe>)AllRecipeTypes.MILLING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<BasinRecipe>)AllRecipeTypes.BASIN.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<MixingRecipe>)AllRecipeTypes.MIXING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<CompactingRecipe>)AllRecipeTypes.COMPACTING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<PressingRecipe>)AllRecipeTypes.PRESSING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<SandPaperPolishingRecipe>)AllRecipeTypes.SANDPAPER_POLISHING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<SplashingRecipe>)AllRecipeTypes.SPLASHING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<DeployerApplicationRecipe>)AllRecipeTypes.DEPLOYING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<FillingRecipe>)AllRecipeTypes.FILLING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate((IRecipeType<EmptyingRecipe>)AllRecipeTypes.EMPTYING.getType(), RecipeSearchUtils.buildPredicate(ProcessingRecipe::getRollableResultsAsItemStacks));
        RecipeRegistryToolkit.registerRecipePredicate(AllRecipeTypes.SEQUENCED_ASSEMBLY.getType(), RecipeSearchUtils.buildPredicateSingle(SequencedAssemblyRecipe::getResultItem));


    }
}
