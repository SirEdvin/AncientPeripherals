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
import site.siredvin.progressiveperipherals.extra.recipes.IRecipeTransformer;
import site.siredvin.progressiveperipherals.extra.recipes.NBTCheckMode;
import site.siredvin.progressiveperipherals.extra.recipes.RecipeRegistryToolkit;
import site.siredvin.progressiveperipherals.extra.recipes.ReflectionRecipeTransformer;

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

        Optional<Item> optTargetItem = Registry.ITEM.getOptional(new ResourceLocation(itemID));

        if (!optTargetItem.isPresent())
            throw new LuaException(String.format("Cannot find item with id %s", itemID));

        ItemStack targetResult = new ItemStack(optTargetItem.get());

        List<IRecipeType<?>> recipeTypes;
        if (types instanceof String) {
            recipeTypes = Collections.singletonList(RecipeRegistryToolkit.getRecipeType((String) types));
        } else if (types instanceof Map) {
            recipeTypes = new ArrayList<>();
            for (Object el: ((Map<?, ?>) types).values()) {
                recipeTypes.add(RecipeRegistryToolkit.getRecipeType(el.toString()));
            }
        } else if (types == null) {
            recipeTypes = Registry.RECIPE_TYPE.stream().collect(Collectors.toList());
        } else {
            throw new LuaException("types should be string or table!");
        }

        trackOperation(QUERY_REGISTRY, null);
        return MethodResult.of(
                recipeTypes.stream().flatMap(recipeType -> RecipeRegistryToolkit.findRecipesForType(recipeType, targetResult, getWorld(), NBTCheckMode.SUBSET).stream())
                        .map(RecipeRegistryToolkit::serializeRecipe).collect(Collectors.toList())
        );
    }
}
