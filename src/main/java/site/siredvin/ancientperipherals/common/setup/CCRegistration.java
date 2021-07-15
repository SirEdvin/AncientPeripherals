package site.siredvin.ancientperipherals.common.setup;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade;
import net.minecraft.enchantment.Enchantments;
import net.minecraftforge.fml.common.Mod;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.computercraft.turtles.*;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AncientPeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCRegistration {

    public static List<AbstractTurtleUpgrade> turtleUpgrades;

    public static void register() {
        registerPocketUpgrades();
        registerTurtleUpgrades();
    }

    private static void registerPocketUpgrades() {
    }

    private static void registerTurtleUpgrades() {
        turtleUpgrades = new ArrayList<AbstractTurtleUpgrade>() {{
            add(new ScientificTurtle());
            add(new EnchantingTurtle());
            add(new SmithingTurtle());
            add(new TurtleCuttingAxe());
            add(new TurtleExtractingPickaxe());
            add(TurtleCuttingAxe.enchant("silk_", Enchantments.SILK_TOUCH, 1));
            add(TurtleExtractingPickaxe.enchant("silk_", Enchantments.SILK_TOUCH, 1));
            add(TurtleExtractingPickaxe.enchant("fortune_1_", Enchantments.BLOCK_FORTUNE, 1));
            add(TurtleExtractingPickaxe.enchant("fortune_2_", Enchantments.BLOCK_FORTUNE, 2));
            add(TurtleExtractingPickaxe.enchant("fortune_3_", Enchantments.BLOCK_FORTUNE, 3));
        }};
        turtleUpgrades.forEach(ComputerCraftAPI::registerTurtleUpgrade);
    }

}