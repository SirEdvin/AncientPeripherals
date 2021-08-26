package site.siredvin.progressiveperipherals.integrations.botania;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;
import vazkii.botania.common.crafting.RecipeBrew;
import vazkii.botania.common.crafting.RecipeElvenTrade;
import vazkii.botania.common.crafting.RecipePureDaisy;
import vazkii.botania.common.item.ModItems;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {
    @Override
    public void run() {
        // recipe transformers
        RecipeRegistryToolkit.registerRecipeSerializer(RecipePureDaisy.class, new RecipeTransformer<RecipePureDaisy>() {
            @Override
            public List<?> getInputs(RecipePureDaisy recipe) {
                return recipe.getInput().getDisplayed().stream().map(state -> new ItemStack(state.getBlock().asItem())).collect(Collectors.toList());
            }

            @Override
            public List<?> getOutputs(RecipePureDaisy recipe) {
                return Collections.singletonList(new ItemStack(recipe.getOutputState().getBlock().asItem()));
            }

            @Override
            public Map<String, Object> getExtraData(RecipePureDaisy recipe) {
                return new HashMap<String, Object>() {{
                    put("time", recipe.getTime());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeElvenTrade.class, new RecipeTransformer<RecipeElvenTrade>() {
            @Override
            public List<?> getInputs(RecipeElvenTrade recipe) {
                return recipe.getIngredients();
            }

            @Override
            public List<?> getOutputs(RecipeElvenTrade recipe) {
                return recipe.getOutputs();
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(RecipeBrew.class, new RecipeTransformer<RecipeBrew>() {
            @Override
            public List<?> getInputs(RecipeBrew recipe) {
                return recipe.getIngredients();
            }

            @Override
            public List<?> getOutputs(RecipeBrew recipe) {
                return Collections.singletonList(recipe.getOutput(new ItemStack(ModItems.brewVial.asItem())));
            }

            @Override
            public Map<String, Object> getExtraData(RecipeBrew recipe) {
                return new HashMap<String, Object>() {{
                    put("mana", recipe.getBrew().getManaCost());
                }};
            }
        });

        // searchers
        RecipeRegistryToolkit.registerRecipePredicate(
            (IRecipeType<RecipeElvenTrade>) Registry.RECIPE_TYPE.getOptional(RecipeElvenTrade.TYPE_ID).get(),
            RecipeSearchUtils.buildPredicate(RecipeElvenTrade::getOutputs)
        );
        RecipeRegistryToolkit.registerRecipePredicate(
            (IRecipeType<RecipePureDaisy>) Registry.RECIPE_TYPE.getOptional(RecipePureDaisy.TYPE_ID).get(),
            (stack, recipe, checkMode) -> recipe.getOutputState().getBlock().asItem() == stack.getItem()
        );
    }
}
