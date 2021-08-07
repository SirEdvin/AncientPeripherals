package site.siredvin.progressiveperipherals.common.setup;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.items.base.ArmorMaterial;
import site.siredvin.progressiveperipherals.common.items.base.BaseItem;
import site.siredvin.progressiveperipherals.common.items.base.ItemTier;
import site.siredvin.progressiveperipherals.common.items.*;
import site.siredvin.progressiveperipherals.common.items.peripheral.EnchantablePeripheralItem;
import site.siredvin.progressiveperipherals.common.items.peripheral.PeripheralItem;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.EnderwireNetworkManagementPocket;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.*;

import java.util.HashSet;

public class Items {

    // Automata cores
    public static final RegistryObject<Item> FORGED_AUTOMATA_CORE = Registration.ITEMS.register("forged_automata_core", ForgedAutomataCore::new);

    public static final RegistryObject<Item> SCIENTIFIC_AUTOMATA_CORE = Registration.ITEMS.register(
            "scientific_automata_core", () -> new PeripheralItem(() -> ProgressivePeripheralsConfig.enableScientificAutomataCore, ScientificTurtle.ID, null)
    );

    public static final RegistryObject<Item> ENCHANTING_AUTOMATA_CORE = Registration.ITEMS.register(
            "enchanting_automata_core", () -> new PeripheralItem(() -> ProgressivePeripheralsConfig.enableEnchantingAutomataCore, EnchantingTurtle.ID, null)
    );

    public static final RegistryObject<Item> SMITHING_AUTOMATA_CORE = Registration.ITEMS.register(
            "smithing_automata_core", () -> new PeripheralItem(() -> ProgressivePeripheralsConfig.enableSmithingAutomataCore, SmithingTurtle.ID, null)
    );

    public static final RegistryObject<Item> BREWING_AUTOMATA_CORE = Registration.ITEMS.register(
            "brewing_automata_core", () -> new PeripheralItem(() -> ProgressivePeripheralsConfig.enableBrewingAutomataCore, BrewingTurtle.ID, null)
    );

    // Abstractium
    public static final RegistryObject<Item> ABSTRACTIUM_INGOT = Registration.ITEMS.register(
            "abstractium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_SWORD = Registration.ITEMS.register(
            "abstractium_sword", () -> new SwordItem(ItemTier.ABSTRACTIUM, 3, -2.4F, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_SHOVEL = Registration.ITEMS.register(
            "abstractium_shovel", () -> new ShovelItem(ItemTier.ABSTRACTIUM, 1.5F, -3.0F, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_PICKAXE = Registration.ITEMS.register(
            "abstractium_pickaxe", () -> new PickaxeItem(ItemTier.ABSTRACTIUM, 1, -2.8F, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_AXE = Registration.ITEMS.register(
            "abstractium_axe", () -> new AxeItem(ItemTier.ABSTRACTIUM, 6.0F, -3.0F, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_HOE = Registration.ITEMS.register(
            "abstractium_hoe", () -> new HoeItem(ItemTier.ABSTRACTIUM, 0, -3.0F, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );

    public static final RegistryObject<Item> ABSTRACTIUM_HELMET = Registration.ITEMS.register(
            "abstractium_helmet", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.HEAD, (new Item.Properties()).tab(ProgressivePeripherals.TAB)));
    public static final RegistryObject<Item> ABSTRACTIUM_CHESTPLATE = Registration.ITEMS.register(
            "abstractium_chestplate", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.CHEST, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_LEGGINGS = Registration.ITEMS.register(
            "abstractium_leggings", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.LEGS, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_BOOTS = Registration.ITEMS.register(
            "abstractium_boots", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.FEET, (new Item.Properties()).tab(ProgressivePeripherals.TAB))
    );

    // Usefull abstractium tools
    public static final RegistryObject<Item> CUTTING_AXE = Registration.ITEMS.register(
            "cutting_axe", () -> new EnchantablePeripheralItem(() -> ProgressivePeripheralsConfig.enableCuttingAxe, new HashSet<Enchantment>(){{
                add(Enchantments.SILK_TOUCH);
            }}, TurtleCuttingAxe.ID, null)
    );

    public static final RegistryObject<Item> EXTRACTING_PICKAXE = Registration.ITEMS.register(
            "extracting_pickaxe", () -> new EnchantablePeripheralItem(() -> ProgressivePeripheralsConfig.enableExtractingPickaxe, new HashSet<Enchantment>(){{
                add(Enchantments.BLOCK_FORTUNE);
                add(Enchantments.SILK_TOUCH);
            }}, TurtleExtractingPickaxe.ID, null)
    );

    public static final RegistryObject<Item> CORRECTING_SHOVEL = Registration.ITEMS.register(
            "correcting_shovel", () -> new PeripheralItem(() -> ProgressivePeripheralsConfig.enableCorrectingShovel, TurtleCorrectingShovel.ID, null)
    );

    // Irrealium
    public static final RegistryObject<Item> IRREALIUM_INGOT = Registration.ITEMS.register(
            "irrealium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Item> KNOWLEDGIUM_INGOT = Registration.ITEMS.register(
            "knowledgium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64))
    );

    // Enderwire network

    public static final RegistryObject<Item> ENREDIUM_INGOT = Registration.ITEMS.register(
            "enredium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Item> ENDERWIRE_NETWORK_MANAGER = Registration.ITEMS.register(
            "enderwire/network_manager", () -> new PeripheralItem(() -> true, null, EnderwireNetworkManagementPocket.ID)
    );

    public static void register() {
    }


}
