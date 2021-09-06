package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TileEntityPeripheralOwner;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.RecipeRegistryTileEntity;
import site.siredvin.progressiveperipherals.extra.recipes.*;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUERY_REGISTRY;

public class RecipeRegistryPeripheral extends BasePeripheral<TileEntityPeripheralOwner<RecipeRegistryTileEntity>> {
    public static final String TYPE = "recipeRegistry";

    public RecipeRegistryPeripheral(RecipeRegistryTileEntity tileEntity) {
        super(TYPE, new TileEntityPeripheralOwner<>(tileEntity));
        owner.attachOperation(QUERY_REGISTRY);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableRecipeRegistry;
    }

    protected IRecipeTransformer<IRecipe<?>> buildTransformer(@Nullable Map<?, ?> transformationData) throws LuaException {
        if (transformationData != null) {
            return ReflectionRecipeTransformer.build(transformationData);
        }
        return RecipeRegistryToolkit.GENERAL_RECIPE_TRANSFORMER;
    }

    @LuaFunction
    public final MethodResult getRecipeTypes() throws LuaException {
        return withOperation(QUERY_REGISTRY, null, null,
                ignored -> MethodResult.of(Registry.RECIPE_TYPE.keySet().stream().map(ResourceLocation::toString)
                        .filter(type -> !ProgressivePeripheralsConfig.recipeRegistryTypesBlacklist.contains(type)).collect(Collectors.toList())), null);
    }

    @LuaFunction
    public final MethodResult inspectRecipeType(String recipeTypeName) throws LuaException {
        return withOperation(QUERY_REGISTRY, null, null, ignored -> {
            ResourceLocation recipeTypeID = LuaUtils.toResourceLocation(recipeTypeName);

            IRecipeType<?> recipeType = RecipeRegistryToolkit.getRecipeType(recipeTypeID);
            Optional<IRecipe<?>> recipe = RecipeRegistryToolkit.getRecipesForType(recipeType, getWorld()).stream().findFirst();
            return recipe.map(iRecipe -> MethodResult.of(ReflectionUtil.expandObject(iRecipe))).orElseGet(() -> MethodResult.of(null, String.format("No recipe for %s exists", recipeTypeName)));
        }, null);
    }

    @LuaFunction
    public final MethodResult getAllRecipesForType(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUERY_REGISTRY, null, null, ignored -> {
            ResourceLocation recipeTypeID = LuaUtils.getResourceLocation(arguments, 0);
            IRecipeTransformer<IRecipe<?>> transformer = buildTransformer(arguments.optTable(1, null));
            IRecipeType<?> type = RecipeRegistryToolkit.getRecipeType(recipeTypeID);
            return MethodResult.of(RecipeRegistryToolkit.getRecipesForType(type, getWorld()).stream()
                    .map(transformer::transform).collect(Collectors.toList()));
        }, null);
    }

    @LuaFunction
    public final MethodResult getRecipeForType(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUERY_REGISTRY, null, null, ignored -> {
            ResourceLocation recipeTypeID = LuaUtils.getResourceLocation(arguments, 0);
            ResourceLocation recipeID = LuaUtils.getResourceLocation(arguments, 1);
            IRecipeTransformer<IRecipe<?>> transformer = buildTransformer(arguments.optTable(2, null));
            IRecipeType<?> type = RecipeRegistryToolkit.getRecipeType(recipeTypeID);
            return MethodResult.of(RecipeRegistryToolkit.getRecipesForType(type, getWorld()).stream().filter(recipe -> recipe.getId().equals(recipeID))
                    .map(transformer::transform).collect(Collectors.toList()));
        }, null);
    }

    @LuaFunction
    public final MethodResult getRecipesFor(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUERY_REGISTRY, null, null, ignored -> {

            ResourceLocation itemID = LuaUtils.getResourceLocation(arguments, 0);
            Object types = arguments.get(1);
            IRecipeTransformer<IRecipe<?>> transformer = buildTransformer(arguments.optTable(2, null));

            Optional<Item> optTargetItem = Registry.ITEM.getOptional(itemID);

            if (!optTargetItem.isPresent())
                throw new LuaException(String.format("Cannot find item with id %s", itemID));

            ItemStack targetResult = new ItemStack(optTargetItem.get());

            List<IRecipeType<?>> recipeTypes = RecipeRegistryToolkit.collectRecipeTypes(types);
            return MethodResult.of(
                    recipeTypes.stream().flatMap(recipeType -> RecipeRegistryToolkit.findRecipesForType(recipeType, targetResult, getWorld(), NBTCheckMode.NONE).stream())
                            .map(transformer::transform).collect(Collectors.toList())
            );
        }, null);
    }
}
