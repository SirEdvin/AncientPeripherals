package site.siredvin.ancientperipherals.data.builders;

import com.google.common.collect.Lists;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class TweakedShapelessRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private String group = "";

    public TweakedShapelessRecipeBuilder(IItemProvider result, int count) {
        this.result = result.asItem();
        this.count = count;
    }

    public static TweakedShapelessRecipeBuilder shapeless(IItemProvider result) {
        return shapeless(result, 1);
    }

    public static TweakedShapelessRecipeBuilder shapeless(IItemProvider result, int count) {
        return new TweakedShapelessRecipeBuilder(result, count);
    }

    public TweakedShapelessRecipeBuilder requires(ITag<Item> ingredient) {
        return this.requires(Ingredient.of(ingredient));
    }

    public TweakedShapelessRecipeBuilder requires(IItemProvider ingredient) {
        return this.requires(ingredient, 1);
    }

    public TweakedShapelessRecipeBuilder requires(IItemProvider ingredient, int amount) {
        for(int i = 0; i < amount; ++i) {
            this.requires(Ingredient.of(ingredient));
        }

        return this;
    }

    public TweakedShapelessRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public TweakedShapelessRecipeBuilder requires(Ingredient ingredient, int amount) {
        for(int i = 0; i < amount; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public TweakedShapelessRecipeBuilder group(String p_200490_1_) {
        this.group = p_200490_1_;
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer) {
        this.save(consumer, Registry.ITEM.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this.result, this.count, this.group, this.ingredients));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;

        public Result(ResourceLocation id, Item result, int count, String group, List<Ingredient> ingredients) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.ingredients = ingredients;;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            jsonObject.add("ingredients", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }

            jsonObject.add("result", jsonobject);
        }

        public IRecipeSerializer<?> getType() {
            return IRecipeSerializer.SHAPELESS_RECIPE;
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
