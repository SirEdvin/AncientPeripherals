package site.siredvin.ancientperipherals.common.setup;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.fml.common.Mod;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.computercraft.turtles.*;

@Mod.EventBusSubscriber(modid = AncientPeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCRegistration {

    public static ScientificTurtle scientificTurtle;
    public static EnchantingTurtle enchantingTurtle;
    public static SmithingTurtle smithingTurtle;
    public static TurtleCuttingAxe cuttingAxeTurtle;
    public static TurtleExtractingPickaxe extractingPickaxeTurtle;

    public static void register() {
        registerPocketUpgrades();
        registerTurtleUpgrades();
    }

    private static void registerPocketUpgrades() {
    }

    private static void registerTurtleUpgrades() {
        scientificTurtle = new ScientificTurtle();
        ComputerCraftAPI.registerTurtleUpgrade(scientificTurtle);
        enchantingTurtle = new EnchantingTurtle();
        ComputerCraftAPI.registerTurtleUpgrade(enchantingTurtle);
        smithingTurtle = new SmithingTurtle();
        ComputerCraftAPI.registerTurtleUpgrade(smithingTurtle);
        cuttingAxeTurtle = new TurtleCuttingAxe();
        ComputerCraftAPI.registerTurtleUpgrade(cuttingAxeTurtle);
        extractingPickaxeTurtle = new TurtleExtractingPickaxe();
        ComputerCraftAPI.registerTurtleUpgrade(extractingPickaxeTurtle);
    }

}