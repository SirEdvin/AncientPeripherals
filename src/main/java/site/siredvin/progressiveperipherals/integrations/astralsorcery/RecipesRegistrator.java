package site.siredvin.progressiveperipherals.integrations.astralsorcery;

import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.crafting.recipe.*;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultDropItem;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultSpawnEntity;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.item.ItemStack;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

@SuppressWarnings("unused")
public class RecipesRegistrator implements Runnable {

    @Override
    public void run() {
        // recipe transformer
        RecipeRegistryToolkit.registerRecipeSerializer(WellLiquefaction.class, new RecipeTransformer<WellLiquefaction>() {
            @Override
            public List<?> getInputs(WellLiquefaction recipe) {
                return Collections.singletonList(recipe.getInput());
            }

            @Override
            public List<?> getOutputs(WellLiquefaction recipe) {
                return Collections.singletonList(recipe.getFluidOutput());
            }

            @Override
            public Map<String, Object> getExtraData(WellLiquefaction recipe) {
                return new HashMap<String, Object>() {{
                    put("productionMultiplier", recipe.getProductionMultiplier());
                    put("shatterMultiplier", recipe.getShatterMultiplier());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(LiquidInfusion.class, new RecipeTransformer<LiquidInfusion>() {
            @Override
            public List<?> getInputs(LiquidInfusion recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getItemInput());
                    add(recipe.getLiquidInput());
                }};
            }

            @Override
            public List<?> getOutputs(LiquidInfusion recipe) {
                return Collections.singletonList(recipe.getOutput(ItemStack.EMPTY));
            }

            @Override
            public Map<String, Object> getExtraData(LiquidInfusion recipe) {
                return new HashMap<String, Object>() {{
                    put("consumptionChance", recipe.getConsumptionChance());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(LiquidInteraction.class, new RecipeTransformer<LiquidInteraction>() {
            @Override
            public List<?> getInputs(LiquidInteraction recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.getReactant1());
                    add(recipe.getReactant2());
                }};
            }

            @Override
            public List<?> getOutputs(LiquidInteraction recipe) {
                return Collections.singletonList(recipe.getResult());
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(SimpleAltarRecipe.class, new RecipeTransformer<SimpleAltarRecipe>() {
            @Override
            public List<?> getInputs(SimpleAltarRecipe recipe) {
                JsonObject serializedData = new JsonObject();
                recipe.getInputs().serialize(serializedData);
                return Collections.singletonList(serializedData);
            }

            @Override
            public List<?> getOutputs(SimpleAltarRecipe recipe) {
                return recipe.getOutputs(null);
            }

            @Override
            public Map<String, Object> getExtraData(SimpleAltarRecipe recipe) {
                return new HashMap<String, Object>() {{
                    put("starlightRequirement", recipe.getStarlightRequirement());
                    put("duration", recipe.getDuration());
                    put("altarTye", recipe.getAltarType().name());
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(BlockTransmutation.class, new RecipeTransformer<BlockTransmutation>() {
            @Override
            public List<?> getInputs(BlockTransmutation recipe) {
                return recipe.getInputDisplay();
            }

            @Override
            public List<?> getOutputs(BlockTransmutation recipe) {
                return Collections.singletonList(recipe.getOutputDisplay());
            }

            @Override
            public Map<String, Object> getExtraData(BlockTransmutation recipe) {
                return new HashMap<String, Object>() {{
                    put("starlightRequirement", recipe.getStarlightRequired());
                }};
            }
        });

        // result serializers

        RecipeRegistryToolkit.registerSerializer(InteractionResult.class, result -> {
            if (result instanceof ResultDropItem)
                return RecipeRegistryToolkit.serialize(((ResultDropItem) result).getOutput());
            if (result instanceof ResultSpawnEntity)
                return new HashMap<String, Object>() {{
                    put("type", "spawn entity");
                    put("entity_type", ((ResultSpawnEntity) result).getEntityType().getRegistryName().toString());
                }};
            return null;
        });

        // register searchers
        RecipeRegistryToolkit.registerRecipePredicate(RecipeTypesAS.TYPE_ALTAR.getType(), RecipeSearchUtils.buildPredicate(simpleAltarRecipe -> simpleAltarRecipe.getOutputs(null)));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeTypesAS.TYPE_INFUSION.getType(), RecipeSearchUtils.buildPredicateSingle(liquidInfusion -> liquidInfusion.getOutput(ItemStack.EMPTY)));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.getType(), RecipeSearchUtils.buildPredicateSingle(BlockTransmutation::getOutputDisplay));
        RecipeRegistryToolkit.registerRecipePredicate(RecipeTypesAS.TYPE_LIQUID_INTERACTION.getType(), RecipeSearchUtils.buildPredicate(liquidInteraction -> {
            InteractionResult result = liquidInteraction.getResult();
            if (result instanceof ResultDropItem)
                return Collections.singleton(((ResultDropItem) result).getOutput());
            return Collections.emptyList();
        }));
    }
}
