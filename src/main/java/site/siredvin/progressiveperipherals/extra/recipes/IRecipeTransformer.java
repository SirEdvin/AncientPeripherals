package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.item.crafting.IRecipe;

import java.util.Map;

@FunctionalInterface
public interface IRecipeTransformer<T extends IRecipe<?>> {
    Map<String, Object> transform(T recipe);
}
