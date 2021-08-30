package site.siredvin.progressiveperipherals.integrations.ma;

import com.ma.api.rituals.IRitualReagent;
import com.ma.recipes.ItemAndPatternRecipe;
import com.ma.recipes.RecipeInit;
import com.ma.recipes.arcanefurnace.ArcaneFurnaceRecipe;
import com.ma.recipes.rituals.RitualRecipe;
import net.minecraft.nbt.CompoundNBT;
import site.siredvin.progressiveperipherals.extra.recipes.NBTCheckMode;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;
import java.util.stream.Collectors;

public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        // serializes
        RecipeRegistryToolkit.registerRecipeSerializer(RitualRecipe.class, new RecipeTransformer<RitualRecipe>() {
            @Override
            public List<?> getInputs(RitualRecipe recipe) {
                List<Object> data = new ArrayList<>();
                for (IRitualReagent[] reagents: recipe.getReagents())
                    Collections.addAll(data, reagents);
                return data;
            }

            @Override
            public List<?> getOutputs(RitualRecipe recipe) {
                return Collections.singletonList(recipe.getResultItem());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ItemAndPatternRecipe.class, new RecipeTransformer<ItemAndPatternRecipe>() {
            @Override
            public List<?> getInputs(ItemAndPatternRecipe recipe) {
                return Arrays.stream(recipe.getRequiredItems()).map(rl -> new HashMap<String, Object>() {{ put("item", rl.toString());}}).collect(Collectors.toList());
            }

            @Override
            public List<?> getOutputs(ItemAndPatternRecipe recipe) {
                return Collections.singletonList(new HashMap<String, Object>() {{
                    put("item", recipe.getOutput().toString());
                    put("count", recipe.getOutputQuantity());
                }});
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(ArcaneFurnaceRecipe.class, new RecipeTransformer<ArcaneFurnaceRecipe>() {
            @Override
            public List<?> getInputs(ArcaneFurnaceRecipe recipe) {
                return Collections.singletonList(new HashMap<String, Object>() {{ put("item", recipe.getInputItem().toString());}});
            }

            @Override
            public List<?> getOutputs(ArcaneFurnaceRecipe recipe) {
                return Collections.singletonList(new HashMap<String, Object>() {{ put("item", recipe.getOutputItem().toString());}});
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(IRitualReagent.class, reagent -> new HashMap<String, Object>(){{
            put("reagent", reagent.getResourceLocation().toString());
        }});

        // predicates

        RecipeRegistryToolkit.registerRecipePredicate(RecipeInit.RITUAL_TYPE, RecipeSearchUtils.buildPredicateSingle(RitualRecipe::getResultItem));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeInit.MANAWEAVING_RECIPE_TYPE, RecipeSearchUtils.buildPredicateSingle(ItemAndPatternRecipe::getResultItem));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeInit.SHAPE_TYPE, RecipeSearchUtils.buildPredicateSingle(ItemAndPatternRecipe::getResultItem));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeInit.COMPONENT_TYPE, RecipeSearchUtils.buildPredicateSingle(ItemAndPatternRecipe::getResultItem));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeInit.MODIFIER_TYPE, RecipeSearchUtils.buildPredicateSingle(ItemAndPatternRecipe::getResultItem));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeInit.ARCANE_FURNACE_TYPE, (stack, recipe, checkMode) -> {
            CompoundNBT tag = stack.getTag();
            boolean hasTag = tag != null && !tag.isEmpty();
            if (hasTag && checkMode != NBTCheckMode.NONE)
                return false;
            return recipe.getOutputItem().equals(stack.getItem().getRegistryName());
        });
    }
}
