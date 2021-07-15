package site.siredvin.progressiveperipherals.common.setup;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.recipes.AutomataRecipeSerializer;

public class RecipeSerializers {

    public static final RegistryObject<AutomataRecipeSerializer> AUTOMATA_CRAFTING = Registration.RECIPE_SERIALIZERS.register("automata", AutomataRecipeSerializer::new);

    public static void register() {

    }


    public static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(String type)
    {
        ResourceLocation recipeTypeId = new ResourceLocation(ProgressivePeripherals.MOD_ID, type);
        return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new IRecipeType<T>()
        {
            public String toString()
            {
                return type;
            }
        });
    }
}
