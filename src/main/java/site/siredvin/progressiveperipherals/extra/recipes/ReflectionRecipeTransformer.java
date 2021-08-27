package site.siredvin.progressiveperipherals.extra.recipes;

import dan200.computercraft.api.lua.LuaException;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.item.crafting.IRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.*;
import java.util.stream.Collectors;

import static site.siredvin.progressiveperipherals.extra.recipes.ReflectionUtil.extractField;
import static site.siredvin.progressiveperipherals.extra.recipes.ReflectionUtil.extractFields;

public class ReflectionRecipeTransformer extends RecipeTransformer<IRecipe<?>> {

    private final @NotNull List<String> inputFields;
    private final @NotNull List<String> outputFields;
    private final @NotNull Map<String, String> extraFields;

    protected ReflectionRecipeTransformer(@NotNull List<String> inputFields, @NotNull List<String> outputFields, @NotNull Map<String, String> extraFields) {
        this.inputFields = inputFields;
        this.outputFields = outputFields;
        this.extraFields = extraFields;
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
        for (Map.Entry<String, String> fieldName: extraFields.entrySet()) {
            Pair<Boolean, Object> extractedPair = extractField(recipe, recipeClass, fieldName.getValue(), ProgressivePeripheralsConfig.recipeRegistryReflectionAllowedLevel);
            if (extractedPair.getLeft()) {
                data.put(fieldName.getKey(), RecipeRegistryToolkit.serializePossibleCollection(extractedPair.getRight()));
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
        Map<String, String> extraFields;
        if (extraObj == null) {
            extraFields = Collections.emptyMap();
        } else {
            extraFields = (Map<String, String>) extraObj;
        }
        return new ReflectionRecipeTransformer(inputFields, outputFields, extraFields);
    }
}
