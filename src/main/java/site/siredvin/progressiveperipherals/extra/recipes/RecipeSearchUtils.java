package site.siredvin.progressiveperipherals.extra.recipes;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.Collection;
import java.util.function.Function;

public class RecipeSearchUtils {

    public static boolean checkInsideOutput(ItemStack targetingStack, NBTCheckMode checkMode, Collection<ItemStack> output) {
        return output.stream().anyMatch(resultStack -> checkMode.itemStackEquals(resultStack, targetingStack));
    }

    public static boolean checkInsideOutputs(ItemStack targetingStack, NBTCheckMode checkMode, Pair<Collection<ItemStack>, Collection<ItemStack>> outputs) {
        if (checkInsideOutput(targetingStack, checkMode, outputs.getLeft()))
            return true;
        return checkInsideOutput(targetingStack, checkMode, outputs.getRight());
    }

    public static  <T extends IRecipe<?>> RecipeSearchPredicate<T> buildPredicate(Function<T, Collection<ItemStack>> mapper) {
        return (stack, recipe, checkMode) -> checkInsideOutput(stack, checkMode, mapper.apply(recipe));
    }

    public static  <T extends IRecipe<?>> RecipeSearchPredicate<T> buildPredicateForPair(Function<T, Pair<Collection<ItemStack>, Collection<ItemStack>>> mapper) {
        return (stack, recipe, checkMode) -> checkInsideOutputs(stack, checkMode, mapper.apply(recipe));
    }
}
