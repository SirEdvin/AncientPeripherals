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
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ProgressivePeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCRegistration {

    public static List<AbstractTurtleUpgrade> turtleUpgrades;
    public static List<AbstractPocketUpgrade> pocketUpgrades;

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
            add(TurtleCuttingAxe.enchant("silk_", Enchantments.SILK_TOUCH, 1, TurtleDigOperationType.SILK_CUTTING_AXE));
            add(TurtleExtractingPickaxe.enchant("silk_", Enchantments.SILK_TOUCH, 1, TurtleDigOperationType.SILK_EXTRACTING_PICKAXE));
            add(TurtleExtractingPickaxe.enchant("fortune_1_", Enchantments.BLOCK_FORTUNE, 1, TurtleDigOperationType.FORTUNE_I_EXTRACTING_PICKAXE));
            add(TurtleExtractingPickaxe.enchant("fortune_2_", Enchantments.BLOCK_FORTUNE, 2, TurtleDigOperationType.FORTUNE_II_EXTRACTING_PICKAXE));
            add(TurtleExtractingPickaxe.enchant("fortune_3_", Enchantments.BLOCK_FORTUNE, 3, TurtleDigOperationType.FORTUNE_III_EXTRACTING_PICKAXE));
            add(new TurtleCorrectingShovel());
            add(new EnderwireModemTurtle());
        }};
        turtleUpgrades.forEach(ComputerCraftAPI::registerTurtleUpgrade);
    }

}