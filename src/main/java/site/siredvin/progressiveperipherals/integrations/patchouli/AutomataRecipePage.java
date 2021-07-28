package site.siredvin.progressiveperipherals.integrations.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.recipes.AutomataRecipe;
import vazkii.patchouli.client.RenderHelper;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class AutomataRecipePage extends PageDoubleRecipeRegistry<AutomataRecipe> {

    public static final ResourceLocation craftingTexture = new ResourceLocation(ProgressivePeripherals.MOD_ID, "textures/gui/patchouli/automata_crafting.png");

    public AutomataRecipePage() {
        super(AutomataRecipe.TYPE);
    }

    public void renderScaledItemStack(MatrixStack ms, int x, int y, int mouseX, int mouseY, ItemStack stack, int size) {
        if (stack == null || stack.isEmpty()) {
            return;
        }

        ms.pushPose();
        float scaleFactor = (float) size / 16.0f;

        ms.scale(scaleFactor, scaleFactor, scaleFactor);

        int rescaledX = (int) Math.floor(x / scaleFactor);
        int rescaledY = (int) Math.floor(y / scaleFactor);

        RenderHelper.transferMsToGl(ms, () -> {
            parent.getMinecraft().getItemRenderer().renderAndDecorateItem(stack, rescaledX, rescaledY);
            parent.getMinecraft().getItemRenderer().renderGuiItemDecorations(parent.getMinecraft().font, stack, rescaledX, rescaledY);
        });

        ms.popPose();

        if (parent.isMouseInRelativeRange(mouseX, mouseY, x, y, size, size)) {
            parent.setTooltipStack(stack);
        }
    }

    public void renderScaledIngredient(MatrixStack ms, int x, int y, int mouseX, int mouseY, Ingredient ingr, int size) {
        ItemStack[] stacks = ingr.getItems();
        if (stacks.length > 0) {
            renderScaledItemStack(ms, x, y, mouseX, mouseY, stacks[(parent.ticksInBook / 20) % stacks.length], size);
        }
    }

    @Override
    protected void drawRecipe(MatrixStack ms, AutomataRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        mc.textureManager.bind(craftingTexture);
        RenderSystem.enableBlend();
        AbstractGui.blit(ms, recipeX - 2, recipeY - 2, 0, 0, 100, 62, 128, 64);


        parent.drawCenteredStringNoShadow(ms, getTitle(second).getString(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);

        parent.renderItemStack(ms, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getResultItem());

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int wrap = 4;

        for (int i = 0; i < ingredients.size(); i++) {
            renderScaledIngredient(ms, recipeX + (i % wrap) * 14 + 3, recipeY + (i / wrap) * 14 + 3, mouseX, mouseY, ingredients.get(i), 12);
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
