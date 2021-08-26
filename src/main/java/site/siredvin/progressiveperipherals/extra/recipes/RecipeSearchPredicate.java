package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

@FunctionalInterface
public interface RecipeSearchPredicate<T extends IRecipe<?>> {
    boolean test(ItemStack stack, T recipe, NBTCheckMode checkMode);
}
