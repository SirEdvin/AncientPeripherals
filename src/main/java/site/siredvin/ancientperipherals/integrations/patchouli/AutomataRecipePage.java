package site.siredvin.ancientperipherals.integrations.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import site.siredvin.ancientperipherals.common.recipes.AutomataRecipe;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class AutomataRecipePage extends PageDoubleRecipeRegistry<AutomataRecipe>{

    // TODO: Add 4x4 crafting image?)

    public AutomataRecipePage() {
        super(AutomataRecipe.TYPE);
    }

    @Override
    protected void drawRecipe(MatrixStack ms, AutomataRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        mc.textureManager.bind(book.craftingTexture);
        RenderSystem.enableBlend();
        AbstractGui.blit(ms, recipeX - 2, recipeY - 2, 0, 0, 100, 62, 128, 256);


        parent.drawCenteredStringNoShadow(ms, getTitle(second).getString(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);

        parent.renderItemStack(ms, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getResultItem());

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int wrap = 4;

        for (int i = 0; i < ingredients.size(); i++) {
            parent.renderIngredient(ms, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
        }

        parent.renderItemStack(ms, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.getToastSymbol());
    }

    @Override
    protected ItemStack getRecipeOutput(AutomataRecipe recipe) {
        if (recipe == null) {
            return ItemStack.EMPTY;
        }
        return recipe.getResultItem();
    }

    @Override
    protected int getRecipeHeight() {
        return 78;
    }
}
