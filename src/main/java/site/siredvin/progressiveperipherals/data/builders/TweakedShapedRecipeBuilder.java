package site.siredvin.progressiveperipherals.data.builders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class TweakedShapedRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    public TweakedShapedRecipeBuilder(IItemProvider result, int count) {
        this.result = result.asItem();
        this.count = count;
    }

    public static TweakedShapedRecipeBuilder shaped(IItemProvider result) {
        return shaped(result, 1);
    }

    public static TweakedShapedRecipeBuilder shaped(IItemProvider result, int count) {
        return new TweakedShapedRecipeBuilder(result, count);
    }

    public TweakedShapedRecipeBuilder define(Character p_200469_1_, ITag<Item> p_200469_2_) {
        return this.define(p_200469_1_, Ingredient.of(p_200469_2_));
    }

    public TweakedShapedRecipeBuilder define(Character p_200462_1_, IItemProvider p_200462_2_) {
        return this.define(p_200462_1_, Ingredient.of(p_200462_2_));
    }

    public TweakedShapedRecipeBuilder define(Character character, Ingredient ingredient) {
        if (this.key.containsKey(character))
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        if (character == ' ')
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        this.key.put(character, ingredient);
        return this;

    }

    public TweakedShapedRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length())
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        this.rows.add(pattern);
        return this;
    }

    public TweakedShapedRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    public void save(Consumer<IFinishedRecipe> p_200464_1_) {
        this.save(p_200464_1_, Registry.ITEM.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        consumer.accept(new TweakedShapedRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.rows, this.key));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.rows.isEmpty())
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        Set<Character> set = Sets.newHashSet(this.key.keySet());
        set.remove(' ');

        for(String s : this.rows) {
            for(int i = 0; i < s.length(); ++i) {
                char c0 = s.charAt(i);
                if (!this.key.containsKey(c0) && c0 != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
                }

                set.remove(c0);
            }
        }

        if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
        }
        if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
            throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;

        public Result(ResourceLocation id, Item result, int count, String group, List<String> pattern, Map<Character, Ingredient> key) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.pattern = pattern;
            this.key = key;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(String s : this.pattern) {
                jsonarray.add(s);
            }

            jsonObject.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();

            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }

            jsonObject.add("key", jsonobject);
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count);
            }

            jsonObject.add("result", jsonobject1);
        }

        public IRecipeSerializer<?> getType() {
            return IRecipeSerializer.SHAPED_RECIPE;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
