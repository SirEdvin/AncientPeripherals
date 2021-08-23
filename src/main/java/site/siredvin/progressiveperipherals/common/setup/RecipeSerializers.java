package site.siredvin.progressiveperipherals.common.setup;

import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.recipes.AutomataRecipeSerializer;

public class RecipeSerializers {

    public static final RegistryObject<AutomataRecipeSerializer> AUTOMATA_CRAFTING = Registration.RECIPE_SERIALIZERS.register("automata", AutomataRecipeSerializer::new);

    @SuppressWarnings("EmptyMethod")
    public static void register() {

    }
}
