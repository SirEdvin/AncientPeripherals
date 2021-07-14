package site.siredvin.ancientperipherals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import site.siredvin.ancientperipherals.common.setup.Blocks;
import site.siredvin.ancientperipherals.common.setup.Items;

import java.util.function.Consumer;

public class RecipesProvider extends RecipeProvider implements IConditionBuilder {

    public RecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void buildSmithingRecipes(Consumer<IFinishedRecipe> consumer) {
        makeSmithingRecipe(
                de.srendi.advancedperipherals.common.setup.Items.WEAK_AUTOMATA_CORE.get().asItem(),
                net.minecraft.item.Items.NETHERITE_INGOT.getItem(),
                Items.FORGED_AUTOMATA_CORE.get().asItem(), consumer
        );
    }
    protected void buildAutomataRecipes(Consumer<IFinishedRecipe> consumer) {
        AutomataRecipeBuilder.start(Blocks.FLEXIBLE_REALITY_ANCHOR.get(), 32)
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.block.Blocks.STONE_BRICKS.asItem())
                .define('F', net.minecraft.item.Items.IRON_INGOT)
                .pattern("IFFI")
                .pattern("ISSI")
                .pattern("ISSI")
                .pattern("IFFI")
                .save(consumer);
        AutomataRecipeBuilder.start(Blocks.REALITY_FORGER.get())
                .define('B', Blocks.ABSTRACTIUM_BLOCK.get())
                .define('P', de.srendi.advancedperipherals.common.setup.Blocks.PERIPHERAL_CASING.get())
                .define('R', net.minecraft.block.Blocks.REDSTONE_BLOCK)
                .pattern("PB  ")
                .pattern("BR  ")
                .pattern("  R ")
                .pattern("   R")
                .save(consumer);
    }

    protected void buildCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
        TweakedShapelessRecipeBuilder.shapeless(Blocks.ABSTRACTIUM_BLOCK.get())
                .requires(Items.ABSTRACTIUM_INGOT.get(), 9)
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_AXE.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern("II ")
                .pattern("IS ")
                .pattern(" S ")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_HOE.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern("II ")
                .pattern(" S ")
                .pattern(" S ")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_SWORD.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern(" I ")
                .pattern(" I ")
                .pattern(" S ")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_PICKAXE.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_SHOVEL.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern(" I ")
                .pattern(" S ")
                .pattern(" S ")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_BOOTS.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .pattern("   ")
                .pattern("I I")
                .pattern("I I")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_CHESTPLATE.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .pattern("I I")
                .pattern("III")
                .pattern("III")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_HELMET.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .pattern("   ")
                .pattern("III")
                .pattern("I I")
                .save(consumer);
        TweakedShapedRecipeBuilder.shaped(Items.ABSTRACTIUM_LEGGINGS.get())
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .pattern("III")
                .pattern("I I")
                .pattern("I I")
                .save(consumer);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        this.buildCraftingRecipes(consumer);
        this.buildSmithingRecipes(consumer);
        this.buildAutomataRecipes(consumer);
    }

    private void makeSmithingRecipe(Item base, Item addition, Item output, Consumer<IFinishedRecipe> consumer) {
        consumer.accept(SmithingRecipe.of(Ingredient.of(base), Ingredient.of(addition), output));
    }
}
