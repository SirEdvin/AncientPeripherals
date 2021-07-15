package site.siredvin.progressiveperipherals.common.setup;

import de.srendi.advancedperipherals.common.setup.Villagers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Dimension;
import site.siredvin.progressiveperipherals.common.items.ForgedAutomataCore;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.TurtleCorrectingShovel;

public class AdditionalRecipes {

    private static void registerFeedRecipes() {
        ForgedAutomataCore.addForgedSoulRecipe(Villagers.COMPUTER_SCIENTIST.get(), Items.SCIENTIFIC_AUTOMATA_CORE.get());
        ForgedAutomataCore.addForgedSoulRecipe(VillagerProfession.LIBRARIAN, Items.ENCHANTING_AUTOMATA_CORE.get());
        ForgedAutomataCore.addForgedSoulRecipe(VillagerProfession.TOOLSMITH, Items.SMITHING_AUTOMATA_CORE.get());
    }

    private static void registerCorrectingRecipes() {
        // Overworld
        TurtleCorrectingShovel.addResourceMapping(Dimension.OVERWORLD.location(), Blocks.SOUL_SAND.asItem(), new ItemStack(Blocks.SAND.asItem()));
        // Nether
        TurtleCorrectingShovel.addResourceMapping(Dimension.NETHER.location(), ItemTags.SAND, new ItemStack(Blocks.SOUL_SAND.asItem()));
    }

    public static void register() {
        registerCorrectingRecipes();
        registerFeedRecipes();
    }
}
