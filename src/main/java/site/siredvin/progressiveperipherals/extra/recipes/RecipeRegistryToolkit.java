package site.siredvin.progressiveperipherals.extra.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.utils.LuaUtils;
import site.siredvin.progressiveperipherals.utils.Platform;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecipeRegistryToolkit {

    public final static Gson GSON = new Gson();
    public final static Object SERIALIZATION_NULL = new Object();

    private final static Map<Class<? extends IRecipe<?>>, IRecipeTransformer> RECIPE_SERIALIZERS = new HashMap<>();
    private final static Map<Class<?>, Function<Object, Object>> SERIALIZERS = new HashMap<>();
    private final static Map<IRecipeType<?>, RecipeSearchPredicate<?>> RECIPE_PREDICATES = new HashMap<>();

    private final static DefaultRecipeTransformer DEFAULT_RECIPE_TRANSFORMER = new DefaultRecipeTransformer();
    private final static RecipeSearchPredicate<IRecipe<?>> DEFAULT_RECIPE_PREDICATE = (stack, recipe, checkMode) -> checkMode.itemStackEquals(recipe.getResultItem(), stack);

    private static final String[] SUPPORTED_MODS = new String[]{
            "mekanism", "astralsorcery",
            "botania", "integrateddynamics",
            "immersiveengineering", "naturesaura",
            "create", "industrialforegoing",
            "pneumaticcraft", "thermal",
            "tconstruct", "bloodmagic",
            "woot", "ars_nouveau"
    };

    public static final Map<String, String> EXTRA_SUPPORTED_MODS = new HashMap<String, String>() {{
        put("mana-and-artifice", "ma");
    }};

    public static <T extends IRecipe<?>> void registerRecipeSerializer(Class<T> recipeClass, IRecipeTransformer<T> transformer) {
        RECIPE_SERIALIZERS.put(recipeClass, transformer);
    }

    public static <T extends IRecipe<?>> void registerRecipeSerializerRaw(Class<T> recipeClass, RecipeTransformer<?> transformer) {
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
            if (ingredient.isEmpty())
                return new HashMap<>();
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
        registerSerializer(ItemStack.class, item -> {
            if (item.isEmpty())
                return SERIALIZATION_NULL;
            return NBTUtil.toLua(item.serializeNBT());
        });
        registerSerializer(FluidStack.class, fluid -> {
            if (fluid.isEmpty())
                return SERIALIZATION_NULL;
            return NBTUtil.toLua(fluid.writeToNBT(new CompoundNBT()));
        });
        registerSerializer(Item.class, item -> new HashMap<String, Object>(){{ put("item", item.getRegistryName().toString());}});
        registerSerializer(Fluid.class, fluid -> new HashMap<String, Object>(){{ put("fluid", fluid.getRegistryName().toString());}});
        registerSerializer(JsonObject.class, RecipeRegistryToolkit::serializeJson);
        registerSerializer(EntityType.class, entityType -> new HashMap<String, Object>() {{ put("entity", entityType.getRegistryName().toString());}});
        registerSerializer(INBT.class, NBTUtil::toLua);

        registerRecipeSerializer(SmithingRecipe.class, new RecipeTransformer<SmithingRecipe>() {
            @Override
            public List<?> getInputs(SmithingRecipe recipe) {
                return new ArrayList<Object>() {{
                    add(recipe.base);
                    add(recipe.addition);
                }};
            }

            @Override
            public List<?> getOutputs(SmithingRecipe recipe) {
                return Collections.singletonList(recipe.getResultItem());
            }
        });
    }

    public static Object serializeJson(JsonObject obj) {
        return GSON.fromJson(obj, HashMap.class);
    }

    public static @Nullable Object serializePossibleCollection(@Nullable Object obj) {
        if (obj instanceof Collection)
            return LuaUtils.toLua(((Collection<?>) obj).stream().map(RecipeRegistryToolkit::serialize).collect(Collectors.toList()));
        if (obj != null && obj.getClass().isArray())
            return LuaUtils.toLua(Arrays.stream((Object[]) obj).map(RecipeRegistryToolkit::serialize).collect(Collectors.toList()));
        return serialize(obj);
    }

    public static @Nullable Object serialize(@Nullable Object obj) {
        if (obj instanceof IRecipeSerializableRecord)
            return ((IRecipeSerializableRecord) obj).serializeForToolkit();
        for (Class<?> clazz: SERIALIZERS.keySet()) {
            if (clazz.isInstance(obj))
                return SERIALIZERS.get(clazz).apply(clazz.cast(obj));
        }
        return obj;
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

    public static List<IRecipeType<?>> collectRecipeTypes(Object types) throws LuaException {
        if (types instanceof String) {
            String recipeTypesSelector = (String) types;
            if (recipeTypesSelector.contains(":"))
                return Collections.singletonList(RecipeRegistryToolkit.getRecipeType((String) types));
            return Registry.RECIPE_TYPE.stream().filter(p -> p.toString().startsWith(recipeTypesSelector)).collect(Collectors.toList());
        }
        if (types instanceof Map) {
            List<IRecipeType<?>> recipeTypes = new ArrayList<>();
            for (Object el: ((Map<?, ?>) types).values()) {
                recipeTypes.add(RecipeRegistryToolkit.getRecipeType(el.toString()));
            }
            return recipeTypes;
        }
        if (types == null)
            return Registry.RECIPE_TYPE.stream().collect(Collectors.toList());
        throw new LuaException("types should be string or table!");
    }

    public static void registerExtra() {
        for (String supportedMod: SUPPORTED_MODS)
            Platform.maybeLoadIntegration(supportedMod, supportedMod + ".RecipesRegistrator").ifPresent(obj -> ((Runnable) obj).run());
        for (Map.Entry<String, String> supportedMod: EXTRA_SUPPORTED_MODS.entrySet())
            Platform.maybeLoadIntegration(supportedMod.getKey(), supportedMod.getValue() + ".RecipesRegistrator").ifPresent(obj -> ((Runnable) obj).run());
    }
}
