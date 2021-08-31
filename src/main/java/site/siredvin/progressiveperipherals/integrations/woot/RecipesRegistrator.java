package site.siredvin.progressiveperipherals.integrations.woot;

import ipsis.woot.crafting.*;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.*;
import java.util.stream.Collectors;

public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        RecipeRegistryToolkit.registerRecipeSerializer(FactoryRecipe.class, new RecipeTransformer<FactoryRecipe>() {
            @Override
            public List<?> getInputs(FactoryRecipe recipe) {
                return new ArrayList<Object>() {{
                    addAll(recipe.getItems());
                    addAll(recipe.getFluids());
                }};
            }

            @Override
            public List<?> getOutputs(FactoryRecipe recipe) {
                return recipe.getDrops();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(DyeSqueezerRecipe.class, new RecipeTransformer<DyeSqueezerRecipe>() {
            @Override
            public List<?> getInputs(DyeSqueezerRecipe recipe) {
                return Collections.singletonList(recipe.getIngredient());
            }

            @Override
            public List<?> getOutputs(DyeSqueezerRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }

            @Override
            public Map<String, Object> getExtraData(DyeSqueezerRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("red", recipe.getRed());
                    put("yellow", recipe.getRed());
                    put("blue", recipe.getRed());
                    put("white", recipe.getRed());
                    put("energy", recipe.getEnergy());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(InfuserRecipe.class, new RecipeTransformer<InfuserRecipe>() {
            @Override
            public List<?> getInputs(InfuserRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getIngredient());
                    add(recipe.getAugment());
                    add(recipe.getFluidInput());
                }};
            }

            @Override
            public List<?> getOutputs(InfuserRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getOutput());
                }};
            }

            @Override
            public Map<String, Object> getExtraData(InfuserRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("energy", recipe.getEnergy());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(AnvilRecipe.class, new RecipeTransformer<AnvilRecipe>() {
            @Override
            public List<?> getInputs(AnvilRecipe recipe) {
                return new ArrayList<Object>() {{
                   add(recipe.getBaseIngredient());
                   addAll(recipe.getIngredients());
                }};
            }

            @Override
            public List<?> getOutputs(AnvilRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(FluidConvertorRecipe.class, new RecipeTransformer<FluidConvertorRecipe>() {
            @Override
            public List<?> getInputs(FluidConvertorRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getInputFluid());
                    add(recipe.getCatalyst());
                }};
            }

            @Override
            public List<?> getOutputs(FluidConvertorRecipe recipe) {
                return Collections.singletonList(recipe.getOutput());
            }

            @Override
            public Map<String, Object> getExtraData(FluidConvertorRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("energy", recipe.getEnergy());
                }};
            }
        });

        RecipeRegistryToolkit.registerSerializer(FactoryRecipe.Drop.class, drop -> new HashMap<String, Object>(){{
            put("item", RecipeRegistryToolkit.serialize(drop.itemStack));
            put("chances", LuaUtils.toLua(drop.dropChance));
            put("stackSizes", LuaUtils.toLua(drop.stackSizes));
        }});

        RecipeRegistryToolkit.registerRecipePredicate(FactoryRecipe.FACTORY_TYPE, RecipeSearchUtils.buildPredicate(factoryRecipe -> factoryRecipe.getDrops().stream().map(drop -> drop.itemStack).collect(Collectors.toList())));
        RecipeRegistryToolkit.registerRecipePredicate(InfuserRecipe.INFUSER_TYPE, RecipeSearchUtils.buildPredicateSingle(InfuserRecipe::getOutput));
        RecipeRegistryToolkit.registerRecipePredicate(AnvilRecipe.ANVIL_TYPE, RecipeSearchUtils.buildPredicateSingle(AnvilRecipe::getOutput));
    }
}
