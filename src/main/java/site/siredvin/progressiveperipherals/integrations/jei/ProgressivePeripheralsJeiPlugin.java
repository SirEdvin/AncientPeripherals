package site.siredvin.progressiveperipherals.integrations.jei;

import com.google.common.collect.ImmutableSet;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.integrations.jei.automata.AutomataRecipeCategory;
import site.siredvin.progressiveperipherals.common.recipes.AutomataRecipe;
import site.siredvin.progressiveperipherals.common.setup.Items;

import java.util.Objects;
import java.util.Set;

@JeiPlugin
public class ProgressivePeripheralsJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ProgressivePeripherals.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.SCIENTIFIC_AUTOMATA_CORE.get()), AutomataRecipeCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld world = Objects.requireNonNull(mc.level);

        Set<AutomataRecipe> automataRecipes = ImmutableSet.copyOf(world.getRecipeManager().getAllRecipesFor(AutomataRecipe.TYPE));
        registration.addRecipes(automataRecipes, AutomataRecipeCategory.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AutomataRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
}
