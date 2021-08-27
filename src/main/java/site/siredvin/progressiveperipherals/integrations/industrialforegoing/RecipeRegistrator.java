package site.siredvin.progressiveperipherals.integrations.industrialforegoing;

import com.buuz135.industrial.recipe.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeSearchUtils;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeTransformer;

import java.util.*;

@SuppressWarnings({"unused", "unchecked"})
public class RecipeRegistrator implements Runnable {

    @Override
    public void run() {
        // transformers

        RecipeRegistryToolkit.registerRecipeSerializer(FluidExtractorRecipe.class, new RecipeTransformer<FluidExtractorRecipe>() {
            @Override
            public List<?> getInputs(FluidExtractorRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(FluidExtractorRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.output);
                    add(new ItemStack(recipe.result.asItem()));
                }};
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(DissolutionChamberRecipe.class, new RecipeTransformer<DissolutionChamberRecipe>() {
            @Override
            public List<?> getInputs(DissolutionChamberRecipe recipe) {
                List<Object> data = new ArrayList<>();
                Collections.addAll(data, recipe.input);
                data.add(recipe.inputFluid);
                return data;
            }

            @Override
            public List<?> getOutputs(DissolutionChamberRecipe recipe) {
                List<Object> data = new ArrayList<>();
                data.add(recipe.output);
                data.add(recipe.outputFluid);
                return data;
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(CrusherRecipe.class, new RecipeTransformer<CrusherRecipe>() {
            @Override
            public List<?> getInputs(CrusherRecipe recipe) {
                return Collections.singletonList(recipe.input);
            }

            @Override
            public List<?> getOutputs(CrusherRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(LaserDrillOreRecipe.class, new RecipeTransformer<LaserDrillOreRecipe>() {
            @Override
            public List<?> getInputs(LaserDrillOreRecipe recipe) {
                return Collections.singletonList(recipe.catalyst);
            }

            @Override
            public List<?> getOutputs(LaserDrillOreRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(LaserDrillOreRecipe recipe) {
                LaserDrillRarity[] rarities = recipe.rarity;
                Map<String, Object> data = new HashMap<>();
                data.put("rarities", Arrays.stream(rarities).map(rarity -> {
                    Map<String, Object> rarityData = new HashMap<>();
                    rarityData.put("whitelistedBiomes", rarity.whitelist);
                    rarityData.put("blacklisterdBiomes", rarity.blacklist);
                    rarityData.put("depth_max", rarity.depth_max);
                    rarityData.put("depth_min", rarity.depth_min);
                    rarityData.put("weight", rarity.weight);
                    return rarityData;
                }));
                return data;
            }
        });

        RecipeRegistryToolkit.registerRecipeSerializer(LaserDrillFluidRecipe.class, new RecipeTransformer<LaserDrillFluidRecipe>() {

            @Override
            public List<?> getInputs(LaserDrillFluidRecipe recipe) {
                return Collections.singletonList(recipe.catalyst);
            }

            @Override
            public List<?> getOutputs(LaserDrillFluidRecipe recipe) {
                return Collections.singletonList(recipe.output);
            }

            @Override
            public Map<String, Object> getExtraData(LaserDrillFluidRecipe recipe) {
                LaserDrillRarity[] rarities = recipe.rarity;
                Map<String, Object> data = new HashMap<>();
                data.put("rarities", Arrays.stream(rarities).map(rarity -> {
                    Map<String, Object> rarityData = new HashMap<>();
                    rarityData.put("whitelistedBiomes", rarity.whitelist);
                    rarityData.put("blacklisterdBiomes", rarity.blacklist);
                    rarityData.put("depth_max", rarity.depth_max);
                    rarityData.put("depth_min", rarity.depth_min);
                    rarityData.put("weight", rarity.weight);
                    return rarityData;
                }));
                data.put("entity", recipe.entity);
                return data;
            }
        });

        // serializers

        RecipeRegistryToolkit.registerSerializer(Ingredient.IItemList.class, itemList -> RecipeRegistryToolkit.GSON.fromJson(itemList.serialize(), HashMap.class));

        // predicates

        RecipeRegistryToolkit.registerRecipePredicate(
                (IRecipeType<FluidExtractorRecipe>) Registry.RECIPE_TYPE.get(new ResourceLocation("industrialforegoing:fluid_extractor")),
                RecipeSearchUtils.buildPredicateSingle(fluidExtractorRecipe -> new ItemStack(fluidExtractorRecipe.result.asItem()))
        );

        RecipeRegistryToolkit.registerRecipePredicate(
                (IRecipeType<DissolutionChamberRecipe>) Registry.RECIPE_TYPE.get(new ResourceLocation("industrialforegoing:dissolution_chamber")),
                RecipeSearchUtils.buildPredicateSingle(dissolutionChamberRecipe -> dissolutionChamberRecipe.output)
        );

        RecipeRegistryToolkit.registerRecipePredicate(
                (IRecipeType<LaserDrillOreRecipe>) Registry.RECIPE_TYPE.get(new ResourceLocation("industrialforegoing:laser_drill_ore")),
                RecipeSearchUtils.buildPredicate(laserDrillOreRecipe -> Arrays.asList(laserDrillOreRecipe.output.getItems()))
        );

        RecipeRegistryToolkit.registerRecipePredicate(
                (IRecipeType<CrusherRecipe>) Registry.RECIPE_TYPE.get(new ResourceLocation("industrialforegoing:crusher")),
                RecipeSearchUtils.buildPredicate(crusherRecipe -> Arrays.asList(crusherRecipe.output.getItems()))
        );
    }
}
