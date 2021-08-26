package site.siredvin.progressiveperipherals.extra.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
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
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.utils.LuaUtils;
import site.siredvin.progressiveperipherals.utils.Platform;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecipeRegistryToolkit {

    private final static Gson GSON = new Gson();

    private final static Map<Class<? extends IRecipe<?>>, RecipeTransformer> RECIPE_SERIALIZERS = new HashMap<>();
    private final static Map<Class<?>, Function<Object, Object>> SERIALIZERS = new HashMap<>();
    private final static Map<IRecipeType<?>, RecipeSearchPredicate<?>> RECIPE_PREDICATES = new HashMap<>();

    private final static DefaultRecipeTransformer DEFAULT_RECIPE_TRANSFORMER = new DefaultRecipeTransformer();
    private final static RecipeSearchPredicate<IRecipe<?>> DEFAULT_RECIPE_PREDICATE = (stack, recipe, checkMode) -> checkMode.itemStackEquals(recipe.getResultItem(), stack);

    private static final String[] SUPPORTED_MODS = new String[]{
        "mekanism",
        "astralsorcery",
        "botania",
    };

    public static void registerRecipeSerializer(Class<? extends IRecipe<?>> recipeClass, RecipeTransformer<?> transformer) {
        RECIPE_SERIALIZERS.put(recipeClass, transformer);
    }

    public static <T> void registerSerializer(Class<T> clazz, Function<T, Object> serializer) {
        SERIALIZERS.put(clazz, (Function<Object, Object>) serializer);
    }

    public static <T extends IRecipe<?>> void registerRecipePredicate(IRecipeType<T> recipeType, RecipeSearchPredicate<T> searchFunction) {
        RECIPE_PREDICATES.put(recipeType, searchFunction);
    }

    public static IRecipeTransformer<IRecipe<?>> GENERAL_RECIPE_TRANSFORMER = RecipeRegistryToolkit::serializeRecipe;

    static {
        registerSerializer(Ingredient.class, ingredient -> {
            try {
                return GSON.fromJson(ingredient.toJson(), HashMap.class);
            } catch (JsonSyntaxException ignored) {
                try {
                    return LuaUtils.toLua(GSON.fromJson(ingredient.toJson(), ArrayList.class));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        registerSerializer(ItemStack.class, item -> NBTUtil.toLua(item.serializeNBT()));
        registerSerializer(FluidStack.class, fluid -> NBTUtil.toLua(fluid.writeToNBT(new CompoundNBT())));
        registerSerializer(Item.class, item -> new HashMap<String, Object>(){{ put("item", item.getRegistryName().toString());}});
        registerSerializer(Fluid.class, fluid -> new HashMap<String, Object>(){{ put("fluid", fluid.getRegistryName().toString());}});
        registerSerializer(JsonObject.class, json -> GSON.fromJson(json, HashMap.class));
    }

    public static @Nullable Object serialize(@Nullable Object obj) {
        for (Class<?> clazz: SERIALIZERS.keySet()) {
            if (clazz.isInstance(obj))
                return SERIALIZERS.get(clazz).apply(clazz.cast(obj));
        }
        if (obj != null) {
            if (obj instanceof Serializable) {
                try {
                    return GSON.fromJson(GSON.toJson(obj), HashMap.class);
                } catch (JsonSyntaxException ignored) {}
            }
            return obj;
        }
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
        if (ProgressivePeripheralsConfig.recipeRegistryTypesBlacklist.contains(type.toString()))
            throw new LuaException(String.format("Incorrect recipe type %s", type));
        Optional<IRecipeType<?>> optRecipeType = Registry.RECIPE_TYPE.getOptional(type);
        if (!optRecipeType.isPresent())
            throw new LuaException(String.format("Incorrect recipe type %s", type));
        return optRecipeType.get();
    }

    public static List<IRecipe<?>> getRecipesForType(IRecipeType recipeType, @NotNull World world) {
        return (List<IRecipe<?>>) world.getRecipeManager().getAllRecipesFor(recipeType);
    }

    public static List<IRecipe<?>> findRecipesForType(IRecipeType<?> recipeType, @NotNull ItemStack result, @NotNull World world, @NotNull NBTCheckMode checkMode) {
        final RecipeSearchPredicate searchPredicate = RECIPE_PREDICATES.getOrDefault(recipeType, DEFAULT_RECIPE_PREDICATE);
        List<IRecipe<?>> recipes = getRecipesForType(recipeType, world);
        return recipes.stream().filter(recipe -> searchPredicate.test(result, recipe, checkMode)).collect(Collectors.toList());
    }

    public static void registerExtra() {
        for (String supportedMod: SUPPORTED_MODS)
            Platform.maybeLoadIntegration(supportedMod, supportedMod + ".RecipesRegistrator").ifPresent(obj -> ((Runnable) obj).run());
    }
}
