package site.siredvin.progressiveperipherals.integrations.bloodmagic;

import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.*;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;
import wayoftime.bloodmagic.recipe.helper.InputIngredient;

import java.util.*;

public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        // transformers
        RecipeRegistryToolkit.registerRecipeSerializer(RecipeBloodAltar.class, new RecipeTransformer<RecipeBloodAltar>() {
            @Override
            public List<?> getInputs(RecipeBloodAltar recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(RecipeBloodAltar recipe) {
                return Collections.singletonList(recipe.getOutput());
            }

            @Override
            public Map<String, Object> getExtraData(RecipeBloodAltar recipe) {
                return new HashMap<String, Object>() {{
                    put("minimumTier", recipe.getMinimumTier());
                    put("syphon", recipe.getSyphon());
                    put("consumeRate", recipe.getConsumeRate());
                    put("drainRate", recipe.getDrainRate());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeAlchemyArray.class, new RecipeTransformer<RecipeAlchemyArray>() {
            @Override
            public List<?> getInputs(RecipeAlchemyArray recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getBaseInput());
                    add(recipe.getAddedInput());
                }};
            }

            @Override
            public List<?> getOutputs(RecipeAlchemyArray recipe) {
                return Collections.singletonList(recipe.getOutput());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeTartaricForge.class, new RecipeTransformer<RecipeTartaricForge>() {
            @Override
            public List<?> getInputs(RecipeTartaricForge recipe) {
                return recipe.getInput();
            }

            @Override
            public List<?> getOutputs(RecipeTartaricForge recipe) {
                return Collections.singletonList(recipe.getOutput());
            }

            @Override
            public Map<String, Object> getExtraData(RecipeTartaricForge recipe) {
                return new HashMap<String, Object>() {{
                    put("minimumSouls", recipe.getMinimumSouls());
                    put("soulDrain", recipe.getSoulDrain());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeARC.class, new RecipeTransformer<RecipeARC>() {
            @Override
            public List<?> getInputs(RecipeARC recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getInput());
                    add(recipe.getFluidIngredient());
                }};
            }

            @Override
            public List<?> getOutputs(RecipeARC recipe) {
                return new ArrayList<Object>() {{
                    addAll(recipe.getAllListedOutputs());
                    add(recipe.getFluidOutput());
                }};
            }

            @Override
            public Map<String, Object> getExtraData(RecipeARC recipe) {
                return new HashMap<String, Object>() {{
                    put("tool", RecipeRegistryToolkit.serialize(recipe.getTool()));
                    put("consumeIngredient", recipe.getConsumeIngredient());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeAlchemyTable.class, new RecipeTransformer<RecipeAlchemyTable>() {
            @Override
            public List<?> getInputs(RecipeAlchemyTable recipe) {
                return recipe.getInput();
            }

            @Override
            public List<?> getOutputs(RecipeAlchemyTable recipe) {
                return Collections.singletonList(recipe.getOutput());
            }

            @Override
            public Map<String, Object> getExtraData(RecipeAlchemyTable recipe) {
                return new HashMap<String, Object>() {{
                    put("syphon", recipe.getSyphon());
                    put("minimumTier", recipe.getMinimumTier());
                }};
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(FluidStackIngredient.class, InputIngredient::serialize);

        // predicates

        RecipeRegistryToolkit.registerRecipePredicate(BloodMagicRecipeType.ALTAR, RecipeSearchUtils.buildPredicateSingle(RecipeBloodAltar::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(BloodMagicRecipeType.ARRAY, RecipeSearchUtils.buildPredicateSingle(RecipeAlchemyArray::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(BloodMagicRecipeType.TARTARICFORGE, RecipeSearchUtils.buildPredicateSingle(RecipeTartaricForge::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(BloodMagicRecipeType.ARC, RecipeSearchUtils.buildPredicate(RecipeARC::getAllListedOutputs));
        RecipeRegistryToolkit.registerRecipePredicate(BloodMagicRecipeType.ALCHEMYTABLE, RecipeSearchUtils.buildPredicateSingle(RecipeAlchemyTable::getOutput));
    }
}
