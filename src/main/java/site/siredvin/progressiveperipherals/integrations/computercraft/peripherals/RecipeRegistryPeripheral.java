package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.extra.recipes.*;

import java.util.*;
import java.util.stream.Collectors;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUERY_REGISTRY;

public class RecipeRegistryPeripheral extends OperationPeripheral {
    public static final String TYPE = "recipeRegistry";

    public RecipeRegistryPeripheral(PeripheralTileEntity<?> tileEntity) {
        super(TYPE, tileEntity);
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
        return MethodResult.of(Registry.RECIPE_TYPE.keySet().stream().map(ResourceLocation::toString)
                .filter(type -> !ProgressivePeripheralsConfig.recipeRegistryTypesBlacklist.contains(type)).collect(Collectors.toList()));
    }

    @LuaFunction
    public final MethodResult inspectRecipeType(String recipeTypeName) throws LuaException {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();

        IRecipeType<?> recipeType = RecipeRegistryToolkit.collectRecipeTypes(recipeTypeName).get(0);
        trackOperation(QUERY_REGISTRY, null);
        Optional<IRecipe<?>> recipe = RecipeRegistryToolkit.getRecipesForType(recipeType, getWorld()).stream().findFirst();
        return recipe.map(iRecipe -> MethodResult.of(ReflectionUtil.expandObject(iRecipe))).orElseGet(() -> MethodResult.of(null, String.format("No recipe for %s exists", recipeTypeName)));
    }

    @LuaFunction
    public final MethodResult getAllRecipesForType(@NotNull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();

        String recipeType = arguments.getString(0);
        Optional<Map<?, ?>> transformationData = arguments.optTable(1);
        IRecipeTransformer<IRecipe<?>> transformer;

        if (transformationData.isPresent()) {
            transformer = ReflectionRecipeTransformer.build(transformationData.get());
        } else {
            transformer = RecipeRegistryToolkit.GENERAL_RECIPE_TRANSFORMER;
        }

        IRecipeType<?> type = RecipeRegistryToolkit.getRecipeType(recipeType);
        trackOperation(QUERY_REGISTRY, null);
        return MethodResult.of(RecipeRegistryToolkit.getRecipesForType(type, getWorld()).stream()
                .map(transformer::transform).collect(Collectors.toList()));
    }

    @LuaFunction
    public final MethodResult getRecipesFor(@NotNull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();

        String itemID = arguments.getString(0);
        Object types = arguments.get(1);
        Optional<Map<?, ?>> transformationData = arguments.optTable(2);
        IRecipeTransformer<IRecipe<?>> transformer;

        if (transformationData.isPresent()) {
            transformer = ReflectionRecipeTransformer.build(transformationData.get());
        } else {
            transformer = RecipeRegistryToolkit.GENERAL_RECIPE_TRANSFORMER;
        }

        Optional<Item> optTargetItem = Registry.ITEM.getOptional(new ResourceLocation(itemID));

        if (!optTargetItem.isPresent())
            throw new LuaException(String.format("Cannot find item with id %s", itemID));

        ItemStack targetResult = new ItemStack(optTargetItem.get());

        List<IRecipeType<?>> recipeTypes = RecipeRegistryToolkit.collectRecipeTypes(types);

        trackOperation(QUERY_REGISTRY, null);
        return MethodResult.of(
                recipeTypes.stream().flatMap(recipeType -> RecipeRegistryToolkit.findRecipesForType(recipeType, targetResult, getWorld(), NBTCheckMode.NONE).stream())
                        .map(transformer::transform).collect(Collectors.toList())
        );
    }
}
