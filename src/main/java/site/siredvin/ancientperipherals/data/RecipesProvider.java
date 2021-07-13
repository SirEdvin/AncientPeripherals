package site.siredvin.ancientperipherals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
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
    }

    protected void buildCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
        TweakedShapelessRecipeBuilder.shapeless(Blocks.ABSTRACTIUM_BLOCK.get())
                .requires(Items.ABSTRACTIUM_INGOT.get(), 9)
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
