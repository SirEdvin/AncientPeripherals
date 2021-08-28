package site.siredvin.progressiveperipherals.common.setup;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.AbstractPocketUpgrade;
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade;
import net.minecraft.enchantment.Enchantments;
import net.minecraftforge.fml.common.Mod;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.EnderwireModemPocket;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.EnderwireNetworkManagementPocket;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.*;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ProgressivePeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCRegistration {

    public static List<AbstractTurtleUpgrade> turtleUpgrades = new ArrayList<>();
    public static List<AbstractPocketUpgrade> pocketUpgrades = new ArrayList<>();

    public static void register() {
        registerPocketUpgrades();
        registerTurtleUpgrades();
    }

    private static void registerPocketUpgrades() {
        pocketUpgrades = new ArrayList<AbstractPocketUpgrade>() {{
            add(new EnderwireNetworkManagementPocket());
            add(new EnderwireModemPocket());
        }};
        pocketUpgrades.forEach(ComputerCraftAPI::registerPocketUpgrade);
    }

    private static void registerTurtleUpgrades() {
        turtleUpgrades = new ArrayList<AbstractTurtleUpgrade>() {{
            add(new ScientificTurtle());
            add(new EnchantingTurtle());
            add(new SmithingTurtle());
            add(new BrewingTurtle());
            add(new TurtleCuttingAxe());
            add(new TurtleExtractingPickaxe());
            add(TurtleCuttingAxe.enchant("silk_", Enchantments.SILK_TOUCH, 1));
            add(TurtleExtractingPickaxe.enchant("silk_", Enchantments.SILK_TOUCH, 1));
            add(TurtleExtractingPickaxe.enchant("fortune_1_", Enchantments.BLOCK_FORTUNE, 1));
            add(TurtleExtractingPickaxe.enchant("fortune_2_", Enchantments.BLOCK_FORTUNE, 2));
            add(TurtleExtractingPickaxe.enchant("fortune_3_", Enchantments.BLOCK_FORTUNE, 3));
            add(new TurtleCorrectingShovel());
            add(new EnderwireModemTurtle());
            add(TurtleIrrealiumTool.build("irrealium_axe", Items.IRREALIUM_AXE.get()));
            add(TurtleIrrealiumTool.build("silk_irrealium_axe", Items.IRREALIUM_AXE.get(), Enchantments.SILK_TOUCH, 1));
            add(TurtleIrrealiumTool.build("irrealium_shovel", Items.IRREALIUM_SHOVEL.get()));
            add(TurtleIrrealiumTool.build("irrealium_pickaxe", Items.IRREALIUM_PICKAXE.get()));
            add(TurtleIrrealiumTool.build("silk_irrealium_pickaxe", Items.IRREALIUM_PICKAXE.get(), Enchantments.SILK_TOUCH, 1));
            add(TurtleIrrealiumTool.build("fortune_1_irrealium_pickaxe", Items.IRREALIUM_PICKAXE.get(), Enchantments.BLOCK_FORTUNE, 1));
            add(TurtleIrrealiumTool.build("fortune_2_irrealium_pickaxe", Items.IRREALIUM_PICKAXE.get(), Enchantments.BLOCK_FORTUNE, 2));
            add(TurtleIrrealiumTool.build("fortune_3_irrealium_pickaxe", Items.IRREALIUM_PICKAXE.get(), Enchantments.BLOCK_FORTUNE, 3));
            add(new TurtleIrrealiumHand());

        }};
        turtleUpgrades.forEach(ComputerCraftAPI::registerTurtleUpgrade);
    }

}