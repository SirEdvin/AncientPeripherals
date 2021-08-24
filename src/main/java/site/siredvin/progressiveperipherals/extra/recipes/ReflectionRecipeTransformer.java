package site.siredvin.progressiveperipherals.extra.recipes;

import dan200.computercraft.api.lua.LuaException;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.item.crafting.IRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionRecipeTransformer extends RecipeTransformer<IRecipe<?>> {

    private final @NotNull List<String> inputFields;
    private final @NotNull List<String> outputFields;
    private final @NotNull List<String> extraFields;

    protected ReflectionRecipeTransformer(@NotNull List<String> inputFields, @NotNull List<String> outputFields, @NotNull List<String> extraFields) {
        this.inputFields = inputFields;
        this.outputFields = outputFields;
        this.extraFields = extraFields;
    }

    protected Pair<Boolean, Object> extractField(IRecipe<?> recipe, Class<?> recipeClass, String fieldName) {
        try {
            Field field = recipeClass.getField(fieldName);
            return Pair.of(true, field.get(recipe));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        try {
            Method method = recipeClass.getMethod(fieldName);
            return Pair.of(true,  method.invoke(recipe));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }
        return Pair.onlyLeft(false);
    }

    protected List<?> extractFields(IRecipe<?> recipe, List<String> fields) {
        Class<?> recipeClass = recipe.getClass();
        List<Object> extractedFields = new ArrayList<>();
        for (String fieldName: fields) {
            Pair<Boolean, Object> extractedPair = extractField(recipe, recipeClass, fieldName);
            if (extractedPair.getLeft()) {
                Object extractedData = extractedPair.getRight();
                if (extractedData instanceof Collection) {
                    extractedFields.addAll((Collection<?>) extractedData);
                } else {
                    extractedFields.add(extractedData);
                }
            }
        }
        return extractedFields;
    }

    @Override
    public List<?> getInputs(IRecipe<?> recipe) {
        return extractFields(recipe, inputFields);
    }

    @Override
    public List<?> getOutputs(IRecipe<?> recipe) {
        return extractFields(recipe, outputFields);
    }

    public @Nullable Map<String, Object> getExtraData(IRecipe<?> recipe) {
        Class<?> recipeClass = recipe.getClass();
        Map<String, Object> data = new HashMap<>();
        for (String fieldName: extraFields) {
            Pair<Boolean, Object> extractedPair = extractField(recipe, recipeClass, fieldName);
            if (extractedPair.getLeft()) {
                Object extractedData = extractedPair.getRight();
                if (extractedData instanceof Collection) {
                    data.put(fieldName, ((Collection<?>) extractedData).stream().map(RecipeRegistryToolkit::serialize).collect(Collectors.toList()));
                } else {
                    data.put(fieldName, RecipeRegistryToolkit.serialize(extractedData));
                }
            }
        }
        return data;
    }

    public static ReflectionRecipeTransformer build(Map<?, ?> table) throws LuaException {
        if (!table.containsKey("input") || !table.containsKey("output"))
            throw new LuaException("Table should contains input and output fields");
        Object inputObj = table.get("input");
        Object outputObj = table.get("output");
        if (!(inputObj instanceof Map) || !(outputObj instanceof Map))
            throw new LuaException("Input and output should be tables");
        Object extraObj = table.get("extra");
        if (extraObj != null && !(extraObj instanceof Map))
            throw new LuaException("Extra should be table or absent");
        List<String> inputFields = ((Map<?, ?>) inputObj).values().stream().map(Object::toString).collect(Collectors.toList());
        List<String> outputFields = ((Map<?, ?>) outputObj).values().stream().map(Object::toString).collect(Collectors.toList());
        List<String> extraFields;
        if (extraObj == null) {
            extraFields = Collections.emptyList();
        } else {
            extraFields = ((Map<?, ?>) extraObj).values().stream().map(Object::toString).collect(Collectors.toList());
        }
        return new ReflectionRecipeTransformer(inputFields, outputFields, extraFields);
    }
}
