package site.siredvin.progressiveperipherals.common.setup;

import de.srendi.advancedperipherals.common.setup.Villagers;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import site.siredvin.progressiveperipherals.common.items.ForgedAutomataCore;

public class FeedingRecipes {
    public static void register() {
        ForgedAutomataCore.addForgedSoulRecipe(Villagers.COMPUTER_SCIENTIST.get(), Items.SCIENTIFIC_AUTOMATA_CORE.get());
        ForgedAutomataCore.addForgedSoulRecipe(VillagerProfession.LIBRARIAN, Items.ENCHANTING_AUTOMATA_CORE.get());
        ForgedAutomataCore.addForgedSoulRecipe(VillagerProfession.TOOLSMITH, Items.SMITHING_AUTOMATA_CORE.get());
    }
}
