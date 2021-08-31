package site.siredvin.progressiveperipherals.integrations.ars_nouveau;

import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantmentRecipe;
import com.hollingsworth.arsnouveau.api.recipe.GlyphPressRecipe;
import com.hollingsworth.arsnouveau.setup.RecipeRegistry;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

public class RecipesRegistrator implements Runnable {
    @Override
    public void run() {
        RecipeRegistryToolkit.registerRecipeSerializer(GlyphPressRecipe.class, new RecipeTransformer<GlyphPressRecipe>() {
            @Override
            public List<?> getInputs(GlyphPressRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.reagent);
                    add(recipe.getClay());
                }};
            }

            @Override
            public List<?> getOutputs(GlyphPressRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(EnchantingApparatusRecipe.class, new RecipeTransformer<EnchantingApparatusRecipe>() {
            @Override
            public List<?> getInputs(EnchantingApparatusRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.reagent);
                    addAll(recipe.pedestalItems);
                }};
            }

            @Override
            public List<?> getOutputs(EnchantingApparatusRecipe recipe) {
                return Collections.singletonList(recipe.result);
            }

            @Override
            public Map<String, Object> getExtraData(EnchantingApparatusRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("mana", recipe.manaCost);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(EnchantmentRecipe.class, new RecipeTransformer<EnchantmentRecipe>() {
            @Override
            public List<?> getInputs(EnchantmentRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.reagent);
                    addAll(recipe.pedestalItems);
                }};
            }

            @Override
            public List<?> getOutputs(EnchantmentRecipe recipe) {
                return Collections.singletonList(new HashMap<String, Object>() {{
                    put("enchantment", new HashMap<String, Object>() {{
                        put("id", recipe.enchantment.getRegistryName().toString());
                        put("level", recipe.enchantLevel);
                    }});
                }});
            }

            @Override
            public Map<String, Object> getExtraData(EnchantmentRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("mana", recipe.manaCost);
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipePredicate(RecipeRegistry.GLYPH_TYPE, RecipeSearchUtils.buildPredicateSingle(glyphPressRecipe -> glyphPressRecipe.output));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeRegistry.APPARATUS_TYPE, RecipeSearchUtils.buildPredicateSingle(enchantingApparatusRecipe -> enchantingApparatusRecipe.result));
    }
}
