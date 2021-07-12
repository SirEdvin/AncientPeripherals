package site.siredvin.ancientperipherals.common.setup;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.fml.common.Mod;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.computercraft.turtles.TrainableTurtle;

@Mod.EventBusSubscriber(modid = AncientPeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCRegistration {

    public static TrainableTurtle trainableTurtle;

    public static void register() {
        registerPocketUpgrades();
        registerTurtleUpgrades();
    }

    private static void registerPocketUpgrades() {
    }

    private static void registerTurtleUpgrades() {
        trainableTurtle = new TrainableTurtle();
        ComputerCraftAPI.registerTurtleUpgrade(trainableTurtle);
    }

}