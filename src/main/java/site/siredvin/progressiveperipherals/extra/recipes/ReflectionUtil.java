package site.siredvin.progressiveperipherals.extra.recipes;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.item.crafting.IRecipe;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class ReflectionUtil {
    public static Pair<Boolean, Object> extractField(Object targetObj, Class<?> targetClass, String fieldName, int recursionLevel) {
        if (recursionLevel == 0)
            return Pair.onlyLeft(false);
        String[] splitStrings = fieldName.split(Pattern.quote("."), 2);
        String cleanFieldName = splitStrings[0];

        if (ProgressivePeripheralsConfig.recipeRegistryReflectionBlacklist.contains(cleanFieldName))
            return Pair.onlyLeft(false);

        boolean isObjectDiscovered = false;
        Object discoveredObject = null;
        try {
            Field field = targetClass.getField(cleanFieldName);
            discoveredObject = field.get(targetObj);
            isObjectDiscovered = true;
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        try {
            Method method = targetClass.getMethod(cleanFieldName);
            discoveredObject = method.invoke(targetObj);
            isObjectDiscovered = true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }

        if (splitStrings.length > 1 && isObjectDiscovered)
            return extractField(discoveredObject, discoveredObject.getClass(), splitStrings[1], recursionLevel - 1);

        return Pair.of(isObjectDiscovered, discoveredObject);
    }

    public static List<?> extractFields(IRecipe<?> recipe, List<String> fields) {
        Class<?> recipeClass = recipe.getClass();
        List<Object> extractedFields = new ArrayList<>();
        for (String fieldName: fields) {
            Pair<Boolean, Object> extractedPair = extractField(recipe, recipeClass, fieldName, ProgressivePeripheralsConfig.recipeRegistryReflectionAllowedLevel);
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

    public static Map<String, Object> expandObject(IRecipe<?> recipe) {
        Map<String, Object> data = new HashMap<>();
        Class<?> recipeClass = recipe.getClass();
        for (Field field: recipeClass.getFields()) {
            try {
                data.put(field.getName(), RecipeRegistryToolkit.serializePossibleCollection(field.get(recipe)));
            } catch (IllegalAccessException ignored) {}
        }
        for (Method method: recipeClass.getMethods()) {
            if (method.getParameterCount() == 0) {
                try {
                    data.put(method.getName(), RecipeRegistryToolkit.serializePossibleCollection(method.invoke(recipe)));
                } catch (IllegalAccessException | InvocationTargetException ignored) {}
            }
        }
        return data;
    }
}
