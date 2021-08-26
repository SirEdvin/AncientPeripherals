package site.siredvin.progressiveperipherals.integrations.mekanism;

import dan200.computercraft.shared.util.NBTUtil;
import de.srendi.advancedperipherals.common.util.Pair;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.*;
import mekanism.api.recipes.inputs.InputIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    private static final class EnergyRecord {
        private final FloatingLong energy;

        private EnergyRecord(FloatingLong energy) {
            this.energy = energy;
        }

        public FloatingLong getEnergy() {
            return energy;
        }
    }


    @Override
    public void run() {

        // recipe transformers
        RecipeRegistryToolkit.registerRecipeSerializer(ChemicalCrystallizerRecipe.class, new RecipeTransformer<ChemicalCrystallizerRecipe>() {
            @Override
            public List<?> getInputs(ChemicalCrystallizerRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(ChemicalCrystallizerRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ChemicalDissolutionRecipe.class, new RecipeTransformer<ChemicalDissolutionRecipe>() {
            @Override
            public List<?> getInputs(ChemicalDissolutionRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getItemInput());
                result.add(recipe.getGasInput());
                return result;
            }

            @Override
            public List<?> getOutputs(ChemicalDissolutionRecipe recipe) {
                return Collections.singletonList(recipe.getOutputDefinition());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ChemicalInfuserRecipe.class, new RecipeTransformer<ChemicalInfuserRecipe>() {
            @Override
            public List<?> getInputs(ChemicalInfuserRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getLeftInput());
                result.add(recipe.getRightInput());
                return result;
            }

            @Override
            public List<?> getOutputs(ChemicalInfuserRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(CombinerRecipe.class, new RecipeTransformer<CombinerRecipe>() {
            @Override
            public List<?> getInputs(CombinerRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getMainInput());
                result.add(recipe.getExtraInput());
                return result;
            }

            @Override
            public List<?> getOutputs(CombinerRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ElectrolysisRecipe.class, new RecipeTransformer<ElectrolysisRecipe>() {
            @Override
            public List<?> getInputs(ElectrolysisRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(ElectrolysisRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getLeftGasOutputRepresentation());
                result.add(recipe.getRightGasOutputRepresentation());
                return result;
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(FluidSlurryToSlurryRecipe.class, new RecipeTransformer<FluidSlurryToSlurryRecipe>() {
            @Override
            public List<?> getInputs(FluidSlurryToSlurryRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getFluidInput());
                result.add(recipe.getChemicalInput());
                return result;
            }

            @Override
            public List<?> getOutputs(FluidSlurryToSlurryRecipe recipe) {
                return Collections.singletonList(recipe.getOutputRepresentation());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(FluidToFluidRecipe.class, new RecipeTransformer<FluidToFluidRecipe>() {
            @Override
            public List<?> getInputs(FluidToFluidRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(FluidToFluidRecipe recipe) {
                return Collections.singletonList(recipe.getOutputRepresentation());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(GasToGasRecipe.class, new RecipeTransformer<GasToGasRecipe>() {
            @Override
            public List<?> getInputs(GasToGasRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(GasToGasRecipe recipe) {
                return Collections.singletonList(recipe.getOutputRepresentation());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackGasToItemStackRecipe.class, new RecipeTransformer<ItemStackGasToItemStackRecipe>() {
            @Override
            public List<?> getInputs(ItemStackGasToItemStackRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getItemInput());
                result.add(recipe.getChemicalInput());
                return result;
            }

            @Override
            public List<?> getOutputs(ItemStackGasToItemStackRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToEnergyRecipe.class, new RecipeTransformer<ItemStackToEnergyRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToEnergyRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(ItemStackToEnergyRecipe recipe) {
                return Collections.singletonList(new EnergyRecord(recipe.getOutputDefinition()));
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToGasRecipe.class, new RecipeTransformer<ItemStackToGasRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToGasRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(ItemStackToGasRecipe recipe) {
                return Collections.singletonList(recipe.getOutputDefinition());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToInfuseTypeRecipe.class, new RecipeTransformer<ItemStackToInfuseTypeRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToInfuseTypeRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(ItemStackToInfuseTypeRecipe recipe) {
                return Collections.singletonList(recipe.getOutputDefinition());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToItemStackRecipe.class, new RecipeTransformer<ItemStackToItemStackRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToItemStackRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(ItemStackToItemStackRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(MetallurgicInfuserRecipe.class, new RecipeTransformer<MetallurgicInfuserRecipe>() {
            @Override
            public List<?> getInputs(MetallurgicInfuserRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getItemInput());
                result.add(recipe.getInfusionInput());
                return result;
            }

            @Override
            public List<?> getOutputs(MetallurgicInfuserRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(NucleosynthesizingRecipe.class, new RecipeTransformer<NucleosynthesizingRecipe>() {
            @Override
            public List<?> getInputs(NucleosynthesizingRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getItemInput());
                result.add(recipe.getChemicalInput());
                return result;
            }

            @Override
            public List<?> getOutputs(NucleosynthesizingRecipe recipe) {
                return recipe.getOutputDefinition();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(PressurizedReactionRecipe.class, new RecipeTransformer<PressurizedReactionRecipe>() {
            @Override
            public List<?> getInputs(PressurizedReactionRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getInputSolid());
                result.add(recipe.getInputFluid());
                result.add(recipe.getInputGas());
                return result;
            }

            @Override
            public List<?> getOutputs(PressurizedReactionRecipe recipe) {
                org.apache.commons.lang3.tuple.Pair<List<@NonNull ItemStack>, @NonNull GasStack> output = recipe.getOutputDefinition();
                List<Object> result = new ArrayList<>(output.getLeft());
                result.add(output.getRight());
                return result;
            }

            @Override
            public Map<String, Object> getExtraData(PressurizedReactionRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("energy", recipe.getEnergyRequired());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RotaryRecipe.class, new RecipeTransformer<RotaryRecipe>() {
            @Override
            public List<?> getInputs(RotaryRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getGasInput());
                result.add(recipe.getFluidInput());
                return result;
            }

            @Override
            public List<?> getOutputs(RotaryRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.add(recipe.getGasOutputRepresentation());
                result.add(recipe.getFluidOutputRepresentation());
                return result;
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(SawmillRecipe.class, new RecipeTransformer<SawmillRecipe>() {
            @Override
            public List<?> getInputs(SawmillRecipe recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(SawmillRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.addAll(recipe.getMainOutputDefinition());
                result.addAll(recipe.getSecondaryOutputDefinition());
                return result;
            }

            @Override
            public Map<String, Object> getExtraData(SawmillRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("secondaryChance", recipe.getSecondaryChance());
                }};
            }
        });
        // recipe ingredients transfomers
        RecipeRegistryToolkit.registerSerializer(ChemicalStack.class, chemical -> NBTUtil.toLua(chemical.write(new CompoundNBT())));
        RecipeRegistryToolkit.registerSerializer(BoxedChemicalStack.class, boxed -> NBTUtil.toLua(boxed.write(new CompoundNBT())));
        RecipeRegistryToolkit.registerSerializer(EnergyRecord.class, record -> new HashMap<String, Object>(){{ put("energy", record.getEnergy());}});

        RecipeRegistryToolkit.registerSerializer(InputIngredient.class, RecipesRegistrator::unpackIngredient);

        // recipe searchers
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.CRUSHING, RecipeSearchUtils.buildPredicate(ItemStackToItemStackRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.ENRICHING, RecipeSearchUtils.buildPredicate(ItemStackToItemStackRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.SMELTING, RecipeSearchUtils.buildPredicate(ItemStackToItemStackRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.COMBINING, RecipeSearchUtils.buildPredicate(CombinerRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.CRYSTALLIZING, RecipeSearchUtils.buildPredicate(ChemicalCrystallizerRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.COMPRESSING, RecipeSearchUtils.buildPredicate(ItemStackGasToItemStackRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.PURIFYING, RecipeSearchUtils.buildPredicate(ItemStackGasToItemStackRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.INJECTING, RecipeSearchUtils.buildPredicate(ItemStackGasToItemStackRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.NUCLEOSYNTHESIZING, RecipeSearchUtils.buildPredicate(NucleosynthesizingRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.METALLURGIC_INFUSING, RecipeSearchUtils.buildPredicate(MetallurgicInfuserRecipe::getOutputDefinition));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.REACTION, RecipeSearchUtils.buildPredicate(recipe -> recipe.getOutputDefinition().getLeft()));
        RecipeRegistryToolkit.registerRecipePredicate(MekanismRecipeType.SAWING, RecipeSearchUtils.buildPredicateForPair(recipe -> Pair.of(recipe.getMainOutputDefinition(), recipe.getSecondaryOutputDefinition())));
    }

    public static Object unpackIngredient(InputIngredient<?> ingredient) {
        List<?> representation = ingredient.getRepresentations();
        if (representation.size() == 1)
            return RecipeRegistryToolkit.serialize(representation.get(0));
        return Collections.singletonMap("variants", representation.stream().map(RecipeRegistryToolkit::serialize).collect(Collectors.toList()));
    }
}
