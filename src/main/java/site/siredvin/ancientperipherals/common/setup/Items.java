package site.siredvin.ancientperipherals.common.setup;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.items.BaseItem;
import site.siredvin.ancientperipherals.common.items.ForgedAutomataCore;
import site.siredvin.ancientperipherals.common.items.PeripheralItem;
import site.siredvin.ancientperipherals.computercraft.turtles.EnchantingTurtle;
import site.siredvin.ancientperipherals.computercraft.turtles.ScientificTurtle;
import site.siredvin.ancientperipherals.computercraft.turtles.SmithingTurtle;

public class Items {

    public static final RegistryObject<Item> FORGED_AUTOMATA_CORE = Registration.ITEMS.register("forged_automata_core", ForgedAutomataCore::new);
    public static final RegistryObject<Item> SCIENTIFIC_AUTOMATA_CORE = Registration.ITEMS.register(
            "scientific_automata_core", () -> new PeripheralItem(() -> true, ScientificTurtle.ID, null));
    public static final RegistryObject<Item> ENCHANTING_AUTOMATA_CORE = Registration.ITEMS.register(
            "enchanting_automata_core", () -> new PeripheralItem(() -> AncientPeripheralsConfig.enableEnchantingAutomataCore, EnchantingTurtle.ID, null)
    );
    public static final RegistryObject<Item> SMITHING_AUTOMATA_CORE = Registration.ITEMS.register(
            "smithing_automata_core", () -> new PeripheralItem(() -> AncientPeripheralsConfig.enableSmithingAutomataCore, SmithingTurtle.ID, null)
    );
    public static final RegistryObject<Item> ABSTRACTIUM_INGOT = Registration.ITEMS.register("abstractium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64)));

    public static void register() {
    }


}
