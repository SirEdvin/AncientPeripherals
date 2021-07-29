package site.siredvin.progressiveperipherals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.data.builders.AutomataRecipeBuilder;
import site.siredvin.progressiveperipherals.data.builders.SmithingRecipe;
import site.siredvin.progressiveperipherals.data.builders.TweakedShapedRecipeBuilder;
import site.siredvin.progressiveperipherals.data.builders.TweakedShapelessRecipeBuilder;

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

    protected void buildBlockIngotRecipes(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider ingot) {
        TweakedShapelessRecipeBuilder.shapeless(block)
                .requires(ingot, 9)
                .save(consumer);
        TweakedShapelessRecipeBuilder.shapeless(ingot, 9)
                .requires(block)
                .save(consumer);
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
        AutomataRecipeBuilder.start(Blocks.FLEXIBLE_STATUE.get(), 32)
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE.asItem())
                .pattern(" II ")
                .pattern("ISSI")
                .pattern("ISSI")
                .pattern(" II ")
                .save(consumer);
        AutomataRecipeBuilder.start(Blocks.STATUE_WORKBENCH.get())
                .define('B', Blocks.ABSTRACTIUM_BLOCK.get())
                .define('P', de.srendi.advancedperipherals.common.setup.Blocks.PERIPHERAL_CASING.get())
                .define('S', net.minecraft.block.Blocks.STONECUTTER)
                .define('T', net.minecraft.block.Blocks.TORCH)
                .pattern("   T")
                .pattern(" S  ")
                .pattern("BBB ")
                .pattern(" P  ")
                .save(consumer);
        AutomataRecipeBuilder.start(Blocks.ABSTRACTIUM_PEDESTAL.get(), 6)
                .define('I', Items.ABSTRACTIUM_INGOT.get())
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE_SLAB)
                .define('C', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern(" CC ")
                .pattern(" II ")
                .pattern(" CC ")
                .pattern("SSSS")
                .save(consumer);
        AutomataRecipeBuilder.start(Items.CUTTING_AXE.get())
                .define('D', net.minecraft.item.Items.DIAMOND)
                .define('A', Items.ABSTRACTIUM_INGOT.get())
                .define('C', Items.ABSTRACTIUM_AXE.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern("DAA ")
                .pattern("DAS ")
                .pattern(" DC ")
                .pattern("  S ")
                .save(consumer);
        AutomataRecipeBuilder.start(Items.EXTRACTING_PICKAXE.get())
                .define('D', net.minecraft.item.Items.DIAMOND)
                .define('A', Items.ABSTRACTIUM_INGOT.get())
                .define('C', Items.ABSTRACTIUM_PICKAXE.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern("DAAA")
                .pattern(" DSD")
                .pattern("  C ")
                .pattern("  S ")
                .save(consumer);
        AutomataRecipeBuilder.start(Items.CORRECTING_SHOVEL.get())
                .define('D', net.minecraft.item.Items.DIAMOND)
                .define('A', Items.ABSTRACTIUM_INGOT.get())
                .define('C', Items.ABSTRACTIUM_SHOVEL.get())
                .define('S', net.minecraft.item.Items.STICK)
                .pattern("  D ")
                .pattern(" ASA")
                .pattern("  C ")
                .pattern("  S ")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.IRREALIUM_MACHINERY_CASING.get(), 32)
                .define('I', Blocks.IRREALIUM_BLOCK.get())
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern("    ")
                .pattern(" IS ")
                .pattern(" SI ")
                .pattern("    ")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.IRREALIUM_MACHINERY_GLASS.get(), 8)
                .define('I', Blocks.IRREALIUM_BLOCK.get())
                .define('S', net.minecraft.block.Blocks.GLASS)
                .pattern("    ")
                .pattern(" IS ")
                .pattern(" SI ")
                .pattern("    ")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.IRREALIUM_PEDESTAL.get(), 6)
                .define('I', Items.IRREALIUM_INGOT.get())
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE_SLAB)
                .define('C', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern(" CC ")
                .pattern(" II ")
                .pattern(" CC ")
                .pattern("SSSS")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.IRREALIUM_MACHINERY_STORAGE.get())
                .define('I', Items.IRREALIUM_INGOT.get())
                .define('C', net.minecraft.block.Blocks.CHEST)
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern("S  S")
                .pattern("SICS")
                .pattern("SCIS")
                .pattern("S  S")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.IRREALIUM_DOUBLE_MACHINERY_STORAGE.get())
                .define('C', Blocks.IRREALIUM_MACHINERY_STORAGE.get())
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern("    ")
                .pattern(" CS ")
                .pattern(" SC ")
                .pattern("    ")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get())
                .define('C', Blocks.IRREALIUM_MACHINERY_CASING.get())
                .define('I', Items.IRREALIUM_INGOT.get())
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern(" SS ")
                .pattern("SCIS")
                .pattern("SICS")
                .pattern(" SS ")
                .save(consumer);

        AutomataRecipeBuilder.start(Items.KNOWLEDGIUM_INGOT.get(), 16)
                .define('A', Items.ABSTRACTIUM_INGOT.get())
                .define('I', Items.IRREALIUM_INGOT.get())
                .define('B', net.minecraft.item.Items.BOOK)
                .pattern("B  B")
                .pattern(" IA ")
                .pattern(" AI ")
                .pattern("B  B")
                .save(consumer, "automata");

        AutomataRecipeBuilder.start(Blocks.KNOWLEDGIUM_CASING.get())
                .define('K', Items.KNOWLEDGIUM_INGOT.get())
                .define('B', net.minecraft.block.Blocks.BOOKSHELF)
                .define('S', net.minecraft.block.Blocks.SMOOTH_STONE)
                .pattern(" SS ")
                .pattern("KB K")
                .pattern("K  K")
                .pattern(" SS ")
                .save(consumer);

        AutomataRecipeBuilder.start(Blocks.RECIPE_REGISTRY.get())
                .define('C', Blocks.KNOWLEDGIUM_CASING.get())
                .define('B', net.minecraft.block.Blocks.BOOKSHELF)
                .define('T', net.minecraft.block.Blocks.CRAFTING_TABLE)
                .pattern("    ")
                .pattern(" CT ")
                .pattern(" TB ")
                .pattern("    ")
                .save(consumer);
    }

    protected void buildCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
        buildBlockIngotRecipes(consumer, Blocks.ABSTRACTIUM_BLOCK.get(), Items.ABSTRACTIUM_INGOT.get());
        buildBlockIngotRecipes(consumer, Blocks.IRREALIUM_BLOCK.get(), Items.IRREALIUM_INGOT.get());
        buildBlockIngotRecipes(consumer, Blocks.KNOWLEDGIUM_BLOCK.get(), Items.KNOWLEDGIUM_INGOT.get());
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
