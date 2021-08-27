package site.siredvin.progressiveperipherals.integrations.pneumaticcraft;

import com.google.gson.JsonObject;
import me.desht.pneumaticcraft.api.crafting.recipe.*;
import me.desht.pneumaticcraft.common.recipes.PneumaticCraftRecipeType;
import me.desht.pneumaticcraft.common.recipes.amadron.AmadronOffer;
import me.desht.pneumaticcraft.common.recipes.machine.ExplosionCraftingRecipeImpl;
import net.minecraft.item.crafting.Ingredient;
import site.siredvin.progressiveperipherals.extra.recipes.IRecipeSerializableRecord;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

@SuppressWarnings({"unused", "unchecked"})
public class RecipesRegistrator implements Runnable {

    private static class IngredientWithAmountRecord implements IRecipeSerializableRecord {
        private final Ingredient ingredient;
        private final int amount;

        IngredientWithAmountRecord(Ingredient ingredient, int amount) {
            this.ingredient = ingredient;
            this.amount = amount;
        }

        @Override
        public Map<String, Object> serializeForToolkit() {
            return new HashMap<String, Object>() {{
                put("ingredient", RecipeRegistryToolkit.serialize(ingredient));
                put("amount", amount);
            }};
        }
    }

    @Override
    public void run() {
        // transformers

        RecipeRegistryToolkit.registerRecipeSerializer(AmadronOffer.class, recipe -> RecipeRegistryToolkit.GSON.fromJson(recipe.toJson(new JsonObject()), HashMap.class));
        RecipeRegistryToolkit.registerRecipeSerializer(AssemblyRecipe.class, new RecipeTransformer<AssemblyRecipe>() {
            @Override
            public List<?> getInputs(AssemblyRecipe recipe) {
                return Collections.singletonList(new IngredientWithAmountRecord(recipe.getInput(), recipe.getInputAmount()));
            }

            @Override
            public List<?> getOutputs(AssemblyRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ExplosionCraftingRecipe.class, new RecipeTransformer<ExplosionCraftingRecipe>() {
            @Override
            public List<?> getInputs(ExplosionCraftingRecipe recipe) {
                return Collections.singletonList(new IngredientWithAmountRecord(recipe.getInput(), recipe.getAmount()));
            }

            @Override
            public List<?> getOutputs(ExplosionCraftingRecipe recipe) {
                return recipe.getOutputs();
            }

            @Override
            public Map<String, Object> getExtraData(ExplosionCraftingRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("lossRate", recipe.getLossRate());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(HeatFrameCoolingRecipe.class, new RecipeTransformer<HeatFrameCoolingRecipe>() {
            @Override
            public List<?> getInputs(HeatFrameCoolingRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(HeatFrameCoolingRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }

            @Override
            public Map<String, Object> getExtraData(HeatFrameCoolingRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("bonusLimit", recipe.getBonusLimit());
                    put("bonusMultiplier", recipe.getBonusMultiplier());
                    put("thresholdTemperature", recipe.getThresholdTemperature());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(PressureChamberRecipe.class, new RecipeTransformer<PressureChamberRecipe>() {
            @Override
            public List<?> getInputs(PressureChamberRecipe recipe) {
                return recipe.getInputsForDisplay();
            }

            @Override
            public List<?> getOutputs(PressureChamberRecipe recipe) {
                return recipe.getResultsForDisplay();
            }

            @Override
            public Map<String, Object> getExtraData(PressureChamberRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("craftingPressure", recipe.getCraftingPressure());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RefineryRecipe.class, new RecipeTransformer<RefineryRecipe>() {
            @Override
            public List<?> getInputs(RefineryRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(RefineryRecipe recipe) {
                return recipe.getOutputs();
            }

            @Override
            public Map<String, Object> getExtraData(RefineryRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("operatingTemperature", RecipeRegistryToolkit.serializeJson(recipe.getOperatingTemp().toJson()));
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ThermoPlantRecipe.class, new RecipeTransformer<ThermoPlantRecipe>() {
            @Override
            public List<?> getInputs(ThermoPlantRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getInputItem());
                    add(recipe.getInputFluid());
                }};
            }

            @Override
            public List<?> getOutputs(ThermoPlantRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getOutputItem());
                    add(recipe.getOutputFluid());
                }};
            }

            @Override
            public Map<String, Object> getExtraData(ThermoPlantRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("operatingTemperature", RecipeRegistryToolkit.serializeJson(recipe.getOperatingTemperature().toJson()));
                    put("requiredPressure", recipe.getRequiredPressure());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(FluidMixerRecipe.class, new RecipeTransformer<FluidMixerRecipe>() {
            @Override
            public List<?> getInputs(FluidMixerRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getInput1());
                    add(recipe.getInput2());
                }};
            }

            @Override
            public List<?> getOutputs(FluidMixerRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getOutputItem());
                    add(recipe.getOutputFluid());
                }};
            }

            @Override
            public Map<String, Object> getExtraData(FluidMixerRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getProcessingTime());
                    put("requiredPressure", recipe.getRequiredPressure());
                }};
            }
        });

        // predicates

        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.AMADRON_OFFERS, RecipeSearchUtils.buildPredicateSingle(recipe -> recipe.getOutput().getItem()));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.ASSEMBLY_LASER, RecipeSearchUtils.buildPredicateSingle(AssemblyRecipe::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.ASSEMBLY_DRILL, RecipeSearchUtils.buildPredicateSingle(AssemblyRecipe::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.ASSEMBLY_DRILL_LASER, RecipeSearchUtils.buildPredicateSingle(AssemblyRecipe::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.EXPLOSION_CRAFTING, RecipeSearchUtils.buildPredicate(ExplosionCraftingRecipeImpl::getOutputs));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.HEAT_FRAME_COOLING, RecipeSearchUtils.buildPredicateSingle(HeatFrameCoolingRecipe::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.PRESSURE_CHAMBER, RecipeSearchUtils.buildPredicate(PressureChamberRecipe::getResultsForDisplay));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.THERMO_PLANT, RecipeSearchUtils.buildPredicateSingle(ThermoPlantRecipe::getOutputItem));
        RecipeRegistryToolkit.registerRecipePredicate(PneumaticCraftRecipeType.FLUID_MIXER, RecipeSearchUtils.buildPredicateSingle(FluidMixerRecipe::getOutputItem));
    }
}
