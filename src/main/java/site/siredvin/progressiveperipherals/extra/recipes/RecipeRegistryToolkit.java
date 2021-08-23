package site.siredvin.progressiveperipherals.extra.recipes;

import com.google.gson.Gson;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.utils.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecipeRegistryToolkit {

    private final static Map<Class<? extends IRecipe<?>>, RecipeTransformer> RECIPE_SERIALIZERS = new HashMap<>();
    private final static Map<Class<?>, Function<Object, Object>> SERIALIZERS = new HashMap<>();
    private final static Map<IRecipeType<?>, RecipeSearchFunction> RECIPE_SEARCHER = new HashMap<>();

    private final static DefaultRecipeTransformer DEFAULT_RECIPE_TRANSFORMER = new DefaultRecipeTransformer();

    private static final String[] SUPPORTED_MODS = new String[]{
            "mekanism"
    };

    public static void registerRecipeSerializer(Class<? extends IRecipe<?>> recipeClass, RecipeTransformer<?> transformer) {
        RECIPE_SERIALIZERS.put(recipeClass, transformer);
    }

    public static <T> void registerSerializer(Class<T> clazz, Function<T, Object> serializer) {
        SERIALIZERS.put(clazz, (Function<Object, Object>) serializer);
    }

    public static void registerRecipeSearcher(IRecipeType<?> recipeType, RecipeSearchFunction searchFunction) {
        RECIPE_SEARCHER.put(recipeType, searchFunction);
    }

    static {
        registerSerializer(Ingredient.class, ingredient -> new Gson().fromJson(ingredient.toJson(), HashMap.class));
        registerSerializer(ItemStack.class, item -> NBTUtil.toLua(item.serializeNBT()));
        registerSerializer(FluidStack.class, fluid -> NBTUtil.toLua(fluid.writeToNBT(new CompoundNBT())));
    }

    protected static @Nullable Object serialize(@Nullable Object obj) {
        for (Class<?> clazz: SERIALIZERS.keySet()) {
            if (clazz.isInstance(obj))
                return SERIALIZERS.get(clazz).apply(clazz.cast(obj));
        }
        if (obj != null)
            ProgressivePeripherals.LOGGER.error(String.format("Unknown recipe element type: %s", obj.getClass().toString()));
        return null;
    }

    public static Map<String, Object> serializeRecipe(IRecipe<?> recipe) {
        for (Class<? extends IRecipe<?>> recipeClass: RECIPE_SERIALIZERS.keySet()) {
            if (recipeClass.isInstance(recipe))
                return RECIPE_SERIALIZERS.get(recipeClass).transform(recipe);
        }
        return DEFAULT_RECIPE_TRANSFORMER.transform(recipe);
    }

    public static IRecipeType<?> getRecipeType(String type) throws LuaException {
        return getRecipeType(new ResourceLocation(type));
    }

    public static IRecipeType<?> getRecipeType(ResourceLocation type) throws LuaException {
        Optional<IRecipeType<?>> optRecipeType = Registry.RECIPE_TYPE.getOptional(type);
        if (!optRecipeType.isPresent())
            throw new LuaException(String.format("Incorrect recipe type %s", type));
        return optRecipeType.get();
    }

    public static List<IRecipe<?>> getRecipesForType(IRecipeType recipeType, @NotNull World world) {
        return (List<IRecipe<?>>) world.getRecipeManager().getAllRecipesFor(recipeType);
    }

    public static List<IRecipe<?>> findRecipesForType(IRecipeType<?> recipeType, @NotNull ItemStack result, @NotNull World world, @NotNull NBTCheckMode checkMode) {
        RecipeSearchFunction searchFunction = RECIPE_SEARCHER.get(recipeType);
        if (searchFunction != null)
            return searchFunction.search(recipeType, result, world, checkMode);
        List<IRecipe<?>> recipes = getRecipesForType(recipeType, world);
        return recipes.stream().filter(recipe -> checkMode.itemStackEquals(recipe.getResultItem(), result)).collect(Collectors.toList());
    }

    public static void registerExtra() {
        for (String supportedMod: SUPPORTED_MODS)
            Platform.maybeLoadIntegration(supportedMod, supportedMod + ".RecipesRegistrator").ifPresent(obj -> ((Runnable) obj).run());
    }
}
