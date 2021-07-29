package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import com.google.gson.Gson;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.shared.util.NBTUtil;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.*;
import java.util.stream.Collectors;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUERY_REGISTRY;

public class RecipeRegistryPeripheral extends OperationPeripheral {
    public RecipeRegistryPeripheral(String type, PeripheralTileEntity<?> tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(FreeOperation.QUERY_REGISTRY);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableRecipeRegistry;
    }

    @LuaFunction
    public final MethodResult getRecipeTypes() {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();
        trackOperation(QUERY_REGISTRY, null);
        return MethodResult.of(Registry.RECIPE_TYPE.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toList()));
    }

    @LuaFunction
    public final MethodResult getRecipeFor(String recipeTypeName, String itemID) {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();

        Optional<IRecipeType<?>> optRecipeType = Registry.RECIPE_TYPE.getOptional(new ResourceLocation(recipeTypeName));

        if (!optRecipeType.isPresent())
            return MethodResult.of(null, String.format("Cannot find recipe type %s", recipeTypeName));

        Optional<Item> optTargetItem = Registry.ITEM.getOptional(new ResourceLocation(itemID));

        if (!optTargetItem.isPresent())
            return MethodResult.of(null, String.format("Cannot find item %s", itemID));

        trackOperation(QUERY_REGISTRY, null);

        IRecipeType recipeType = optRecipeType.get();

        List<IRecipe<?>> recipes = (List<IRecipe<?>>) getWorld().getRecipeManager().getAllRecipesFor(recipeType);

        Optional<?> optRecipe = recipes.stream()
                .filter(recipe -> recipe.getResultItem() != null && recipe.getResultItem().getItem() == optTargetItem.get()).findAny();
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find any recipe for this combination");

        IRecipe<?> recipe = (IRecipe<?>) optRecipe.get();
        Map<String, Object> recipeData = new HashMap<>();

        recipeData.put("id", recipe.getId().toString());
        recipeData.put("group", recipe.getGroup());
        // Sad reality, any of this can be null
        if (recipe.getResultItem() != null)
            recipeData.put("result", NBTUtil.toLua(recipe.getResultItem().serializeNBT()));
        if (recipe.getIngredients() != null) {
            Map<Integer, Object> ingredientsData = new HashMap<>();
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                ingredientsData.put(i + 1, new Gson().fromJson(ingredients.get(i).toJson(), HashMap.class));
            }
            recipeData.put("ingredients", ingredientsData);
        }

        return MethodResult.of(recipeData);
    }
}
