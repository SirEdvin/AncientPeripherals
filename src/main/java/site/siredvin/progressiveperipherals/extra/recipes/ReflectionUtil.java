package site.siredvin.progressiveperipherals.extra.recipes;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.item.crafting.IRecipe;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReflectionUtil {

    private static final Set<String> BLACKLISTED_NAMES = new HashSet<String>() {{
        add("hashCode");
        add("toString");
    }};

    public static Pair<Boolean, Object> extractField(Object targetObj, Class<?> targetClass, String fieldName, int recursionLevel) {
        if (recursionLevel == 0)
            return Pair.onlyLeft(false);
        String[] splitStrings = fieldName.split(Pattern.quote("."), 2);
        String cleanFieldName = splitStrings[0];

        if (BLACKLISTED_NAMES.contains(cleanFieldName))
            return Pair.onlyLeft(false);

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

        if (splitStrings.length > 1 && isObjectDiscovered) {
            if (discoveredObject instanceof Collection) {
                List<Object> finalResult = new ArrayList<>();
                List<Pair<Boolean, Object>> results = ((Collection<?>) discoveredObject).stream().map(obj -> extractField(obj, obj.getClass(), splitStrings[1], recursionLevel - 1)).collect(Collectors.toList());
                if (!results.stream().allMatch(Pair::getLeft))
                    return Pair.onlyLeft(false);
                results.forEach(pair -> {
                    Object r = pair.getRight();
                    if (r instanceof Collection) {
                        finalResult.addAll((Collection<?>) r);
                    } else {
                        finalResult.add(r);
                    }
                });
                return Pair.of(true, finalResult);
            }
            return extractField(discoveredObject, discoveredObject.getClass(), splitStrings[1], recursionLevel - 1);
        }

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
            if (BLACKLISTED_NAMES.contains(field.getName()))
                continue;
            try {
                data.put(field.getName(), RecipeRegistryToolkit.serializePossibleCollection(field.get(recipe)));
            } catch (IllegalAccessException ignored) {}
        }
        for (Method method: recipeClass.getMethods()) {
            if (BLACKLISTED_NAMES.contains(method.getName()))
                continue;
            if (method.getParameterCount() == 0) {
                try {
                    data.put(method.getName(), RecipeRegistryToolkit.serializePossibleCollection(method.invoke(recipe)));
                } catch (IllegalAccessException | InvocationTargetException ignored) {}
            }
        }
        return data;
    }
}
