package site.siredvin.progressiveperipherals.integrations.mekanism;

import dan200.computercraft.shared.util.NBTUtil;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.tuple.Pair;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    private static final class EnergyRecord {
        private final FloatingLong energy;

        protected EnergyRecord(FloatingLong energy) {
            this.energy = energy;
        }

        public FloatingLong getEnergy() {
            return energy;
        }
    }

    private static final class SecondaryChanceRecord {
        private final double chance;

        protected SecondaryChanceRecord(double chance) {
            this.chance = chance;
        }

        public double getChance() {
            return chance;
        }
    }

    @Override
    public void run() {

        // recipe transformers
        RecipeRegistryToolkit.registerRecipeSerializer(ChemicalCrystallizerRecipe.class, new RecipeTransformer<ChemicalCrystallizerRecipe>() {
            @Override
            public List<?> getInputs(ChemicalCrystallizerRecipe recipe) {
                return recipe.getInput().getRepresentations();
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
                result.addAll(recipe.getItemInput().getRepresentations());
                result.addAll(recipe.getGasInput().getRepresentations());
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
                result.addAll(recipe.getLeftInput().getRepresentations());
                result.addAll(recipe.getRightInput().getRepresentations());
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
                result.addAll(recipe.getMainInput().getRepresentations());
                result.addAll(recipe.getExtraInput().getRepresentations());
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
                return recipe.getInput().getRepresentations();
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
                result.addAll(recipe.getFluidInput().getRepresentations());
                result.addAll(recipe.getChemicalInput().getRepresentations());
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
                return recipe.getInput().getRepresentations();
            }

            @Override
            public List<?> getOutputs(FluidToFluidRecipe recipe) {
                return Collections.singletonList(recipe.getOutputRepresentation());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(GasToGasRecipe.class, new RecipeTransformer<GasToGasRecipe>() {
            @Override
            public List<?> getInputs(GasToGasRecipe recipe) {
                return recipe.getInput().getRepresentations();
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
                result.addAll(recipe.getItemInput().getRepresentations());
                result.addAll(recipe.getChemicalInput().getRepresentations());
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
                return recipe.getInput().getRepresentations();
            }

            @Override
            public List<?> getOutputs(ItemStackToEnergyRecipe recipe) {
                return Collections.singletonList(new EnergyRecord(recipe.getOutputDefinition()));
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToGasRecipe.class, new RecipeTransformer<ItemStackToGasRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToGasRecipe recipe) {
                return recipe.getInput().getRepresentations();
            }

            @Override
            public List<?> getOutputs(ItemStackToGasRecipe recipe) {
                return Collections.singletonList(recipe.getOutputDefinition());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToInfuseTypeRecipe.class, new RecipeTransformer<ItemStackToInfuseTypeRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToInfuseTypeRecipe recipe) {
                return recipe.getInput().getRepresentations();
            }

            @Override
            public List<?> getOutputs(ItemStackToInfuseTypeRecipe recipe) {
                return Collections.singletonList(recipe.getOutputDefinition());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemStackToItemStackRecipe.class, new RecipeTransformer<ItemStackToItemStackRecipe>() {
            @Override
            public List<?> getInputs(ItemStackToItemStackRecipe recipe) {
                return recipe.getInput().getRepresentations();
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
                result.addAll(recipe.getItemInput().getRepresentations());
                result.addAll(recipe.getInfusionInput().getRepresentations());
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
                result.addAll(recipe.getItemInput().getRepresentations());
                result.addAll(recipe.getChemicalInput().getRepresentations());
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
                result.addAll(recipe.getInputSolid().getRepresentations());
                result.addAll(recipe.getInputFluid().getRepresentations());
                result.addAll(recipe.getInputGas().getRepresentations());
                result.add(new EnergyRecord(recipe.getEnergyRequired()));
                return result;
            }

            @Override
            public List<?> getOutputs(PressurizedReactionRecipe recipe) {
                Pair<List<ItemStack>, GasStack> output = recipe.getOutputDefinition();
                List<Object> result = new ArrayList<>(output.getLeft());
                result.add(output.getRight());
                return result;
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RotaryRecipe.class, new RecipeTransformer<RotaryRecipe>() {
            @Override
            public List<?> getInputs(RotaryRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.addAll(recipe.getGasInput().getRepresentations());
                result.addAll(recipe.getFluidInput().getRepresentations());
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
                return recipe.getInput().getRepresentations();
            }

            @Override
            public List<?> getOutputs(SawmillRecipe recipe) {
                List<Object> result = new ArrayList<>();
                result.addAll(recipe.getMainOutputDefinition());
                result.addAll(recipe.getSecondaryOutputDefinition());
                result.add(new SecondaryChanceRecord(recipe.getSecondaryChance()));
                return result;
            }
        });
        // recipe ingredients transfomers
        RecipeRegistryToolkit.registerSerializer(ChemicalStack.class, chemical -> NBTUtil.toLua(chemical.write(new CompoundNBT())));
        RecipeRegistryToolkit.registerSerializer(BoxedChemicalStack.class, boxed -> NBTUtil.toLua(boxed.write(new CompoundNBT())));
        RecipeRegistryToolkit.registerSerializer(EnergyRecord.class, record -> new HashMap<String, Object>(){{
            put("energy", record.getEnergy());
        }});
        RecipeRegistryToolkit.registerSerializer(SecondaryChanceRecord.class, record -> new HashMap<String, Object>(){{
            put("secondaryChance", record.getChance());
        }});
    }
}
