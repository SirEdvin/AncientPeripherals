package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface RecipeSearchFunction {
    List<IRecipe<?>> search(IRecipeType<?> recipeType, @NotNull ItemStack result, @NotNull World world, @NotNull NBTCheckMode checkMode);
}
