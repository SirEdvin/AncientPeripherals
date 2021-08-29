package site.siredvin.progressiveperipherals.integrations.immersiveengineering;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.*;
import dan200.computercraft.shared.util.NBTUtil;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        RecipeRegistryToolkit.registerRecipeSerializer(AlloyRecipe.class, new RecipeTransformer<AlloyRecipe>() {
            @Override
            public List<?> getInputs(AlloyRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.input0);
                    add(recipe.input1);
                }};
            }

            @Override
            public List<?> getOutputs(AlloyRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(AlloyRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(BlastFurnaceRecipe.class, new RecipeTransformer<BlastFurnaceRecipe>() {
            @Override
            public List<?> getInputs(BlastFurnaceRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(BlastFurnaceRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(BlastFurnaceRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                    put("slug", RecipeRegistryToolkit.serialize(recipe.slag));
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(CokeOvenRecipe.class, new RecipeTransformer<CokeOvenRecipe>() {
            @Override
            public List<?> getInputs(CokeOvenRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(CokeOvenRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(CokeOvenRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                    put("creosoteOutput", recipe.creosoteOutput);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ClocheRecipe.class, new RecipeTransformer<ClocheRecipe>() {
            @Override
            public List<?> getInputs(ClocheRecipe recipe) {
                return Collections.singletonList(recipe.seed);
            }

            @Override
            public List<?> getOutputs(ClocheRecipe recipe) {
                return recipe.outputs;
            }

            @Override
            public Map<String, Object> getExtraData(ClocheRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.time);
                    put("soil", RecipeRegistryToolkit.serialize(recipe.soil));
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(BlueprintCraftingRecipe.class, new RecipeTransformer<BlueprintCraftingRecipe>() {
            @Override
            public List<?> getInputs(BlueprintCraftingRecipe recipe) {
                return Arrays.asList(recipe.inputs);
            }

            @Override
            public List<?> getOutputs(BlueprintCraftingRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(BlueprintCraftingRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("blueprintCategory", recipe.blueprintCategory);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(MetalPressRecipe.class, new RecipeTransformer<MetalPressRecipe>() {
            @Override
            public List<?> getInputs(MetalPressRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(MetalPressRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(MetalPressRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("mold", recipe.mold);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ArcFurnaceRecipe.class, new RecipeTransformer<ArcFurnaceRecipe>() {
            @Override
            public List<?> getInputs(ArcFurnaceRecipe recipe) {
                List<Object> inputs = new ArrayList<>();
                inputs.add(recipe.input);
                Collections.addAll(inputs, recipe.additives);
                return inputs;
            }

            @Override
            public List<?> getOutputs(ArcFurnaceRecipe recipe) {
                return recipe.output;
            }

            @Override
            public Map<String, Object> getExtraData(ArcFurnaceRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("slug", RecipeRegistryToolkit.serialize(recipe.slag));
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(BottlingMachineRecipe.class, new RecipeTransformer<BottlingMachineRecipe>() {
            @Override
            public List<?> getInputs(BottlingMachineRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.input);
                    add(recipe.fluidInput);
                }};
            }

            @Override
            public List<?> getOutputs(BottlingMachineRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(CrusherRecipe.class, new RecipeTransformer<CrusherRecipe>() {
            @Override
            public List<?> getInputs(CrusherRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(CrusherRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.output);
                    addAll(recipe.secondaryOutputs);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(SawmillRecipe.class, new RecipeTransformer<SawmillRecipe>() {
            @Override
            public List<?> getInputs(SawmillRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(SawmillRecipe recipe) {
                return recipe.getActualItemOutputs(null);
            }

            @Override
            public Map<String, Object> getExtraData(SawmillRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTotalProcessTime());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(FermenterRecipe.class, new RecipeTransformer<FermenterRecipe>() {
            @Override
            public List<?> getInputs(FermenterRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(FermenterRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.itemOutput);
                    add(recipe.fluidOutput);
                }};
            }

            @Override
            public Map<String, Object> getExtraData(FermenterRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTotalProcessTime());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(SqueezerRecipe.class, new RecipeTransformer<SqueezerRecipe>() {
            @Override
            public List<?> getInputs(SqueezerRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(SqueezerRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.itemOutput);
                    add(recipe.fluidOutput);
                }};
            }

            @Override
            public Map<String, Object> getExtraData(SqueezerRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTotalProcessTime());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RefineryRecipe.class, new RecipeTransformer<RefineryRecipe>() {
            @Override
            public List<?> getInputs(RefineryRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.input0);
                    add(recipe.input1);
                }};
            }

            @Override
            public List<?> getOutputs(RefineryRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(RefineryRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTotalProcessTime());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(MixerRecipe.class, new RecipeTransformer<MixerRecipe>() {
            @Override
            public List<?> getInputs(MixerRecipe recipe) {
                return new ArrayList<Object>() {{
                    Collections.addAll(this, recipe.itemInputs);
                    add(recipe.fluidInput);
                }};
            }

            @Override
            public List<?> getOutputs(MixerRecipe recipe) {
                return Collections.singletonList(recipe.fluidOutput);
            }

            @Override
            public Map<String, Object> getExtraData(MixerRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTotalProcessTime());
                }};
            }
        });

        // serializers
        RecipeRegistryToolkit.registerSerializer(IngredientWithSize.class, ingredientWithSize -> new HashMap<String, Object>() {{
            put("ingredient", RecipeRegistryToolkit.serialize(ingredientWithSize.getBaseIngredient()));
            put("count", ingredientWithSize.getCount());
        }});

        RecipeRegistryToolkit.registerSerializer(ComparableItemStack.class, stack -> RecipeRegistryToolkit.serialize(stack.stack));
        RecipeRegistryToolkit.registerSerializer(FluidTagInput.class, tagInput -> RecipeRegistryToolkit.serialize(tagInput.serialize()));
        RecipeRegistryToolkit.registerSerializer(StackWithChance.class, stackWithChance -> NBTUtil.toLua(stackWithChance.writeToNBT()));

        // predicates

        RecipeRegistryToolkit.registerRecipePredicate(AlloyRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(alloyRecipe -> alloyRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(ArcFurnaceRecipe.TYPE, RecipeSearchUtils.buildPredicate(arcFurnaceRecipe -> arcFurnaceRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(BlastFurnaceRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(blastFurnaceRecipe -> blastFurnaceRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(BlueprintCraftingRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(blueprintCraftingRecipe -> blueprintCraftingRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(BottlingMachineRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(bottlingMachineRecipe -> bottlingMachineRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(ClocheRecipe.TYPE, RecipeSearchUtils.buildPredicate(clocheRecipe -> clocheRecipe.outputs));
        RecipeRegistryToolkit.registerRecipePredicate(CokeOvenRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(cokeOvenRecipe -> cokeOvenRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(CrusherRecipe.TYPE, (stack, recipe, checkMode) -> {
            if (checkMode.itemStackEquals(recipe.output, stack))
                return true;
            return recipe.secondaryOutputs.stream().anyMatch(swc -> checkMode.itemStackEquals(swc.getStack(), stack));
        });
        RecipeRegistryToolkit.registerRecipePredicate(FermenterRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(fermenterRecipe -> fermenterRecipe.itemOutput));
        RecipeRegistryToolkit.registerRecipePredicate(MetalPressRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(metalPressRecipe -> metalPressRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(SawmillRecipe.TYPE, RecipeSearchUtils.buildPredicate(sawmillRecipe -> sawmillRecipe.getActualItemOutputs(null)));
        RecipeRegistryToolkit.registerRecipePredicate(SqueezerRecipe.TYPE, RecipeSearchUtils.buildPredicateSingle(squeezerRecipe -> squeezerRecipe.itemOutput));
    }
}
