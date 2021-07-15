package site.siredvin.progressiveperipherals.data.builders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.RecipeSerializers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class AutomataRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private String group = "";

    public AutomataRecipeBuilder(IItemProvider resultItem, int count) {
        this.result = resultItem.asItem();
        this.count = count;
    }

    public static AutomataRecipeBuilder start(IItemProvider resultItem) {
        return start(resultItem, 1);
    }

    public static AutomataRecipeBuilder start(IItemProvider resultItem, int count) {
        return new AutomataRecipeBuilder(resultItem, count);
    }

    public AutomataRecipeBuilder define(Character ch, ITag<Item> ingredient) {
        return this.define(ch, Ingredient.of(ingredient));
    }

    public AutomataRecipeBuilder define(Character ch, IItemProvider ingredient) {
        return this.define(ch, Ingredient.of(ingredient));
    }

    public AutomataRecipeBuilder define(Character ch, Ingredient ingredient) {
        if (this.key.containsKey(ch)) {
            throw new IllegalArgumentException("Symbol '" + ch + "' is already defined!");
        } else if (ch == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.key.put(ch, ingredient);
        return this;
    }

    public AutomataRecipeBuilder pattern(String row) {
        if (row.length() != 4) {
            throw new IllegalArgumentException("Pattern must be the 4 width on every line!");
        }
        this.rows.add(row);
        return this;
    }

    public AutomataRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer) {
        this.save(consumer, Registry.ITEM.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation resultID) {
        this.ensureValid(resultID);
        consumer.accept(new Result(resultID, this.result, this.count, this.group, this.rows, this.key));
    }

    private void ensureValid(ResourceLocation resultID) {
        if (this.rows.size() != 4) {
            throw new IllegalStateException("Automata recipe should contains 4 rows!");
        }
        Set<Character> set = Sets.newHashSet(this.key.keySet());
        set.remove(' ');

        for(String s : this.rows) {
            for(int i = 0; i < s.length(); ++i) {
                char c0 = s.charAt(i);
                if (!this.key.containsKey(c0) && c0 != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + resultID + " uses undefined symbol '" + c0 + "'");
                }

                set.remove(c0);
            }
        }

        if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + resultID);
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> keys;

        public Result(ResourceLocation id, Item result, int count, String group, List<String> pattern, Map<Character, Ingredient> keys) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.pattern = pattern;
            this.keys = keys;
        }

        public void serializeRecipeData(JsonObject targetObject) {
            if (!this.group.isEmpty()) {
                targetObject.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(String s : this.pattern) {
                jsonarray.add(s);
            }

            targetObject.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();

            for(Map.Entry<Character, Ingredient> entry : this.keys.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }

            targetObject.add("key", jsonobject);
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count);
            }

            targetObject.add("result", jsonobject1);
        }

        public IRecipeSerializer<?> getType() {
            return RecipeSerializers.AUTOMATA_CRAFTING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }

        public ResourceLocation getId() {
            return this.id;
        }
    }
}
