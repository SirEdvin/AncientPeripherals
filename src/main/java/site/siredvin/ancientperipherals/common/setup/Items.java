package site.siredvin.ancientperipherals.common.setup;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.items.BaseItem;
import site.siredvin.ancientperipherals.common.items.ForgedAutomataCore;
import site.siredvin.ancientperipherals.common.items.ItemTier;
import site.siredvin.ancientperipherals.common.items.ArmorMaterial;
import site.siredvin.ancientperipherals.common.items.PeripheralItem;
import site.siredvin.ancientperipherals.computercraft.turtles.EnchantingTurtle;
import site.siredvin.ancientperipherals.computercraft.turtles.ScientificTurtle;
import site.siredvin.ancientperipherals.computercraft.turtles.SmithingTurtle;

public class Items {

    // Automata cores
    public static final RegistryObject<Item> FORGED_AUTOMATA_CORE = Registration.ITEMS.register("forged_automata_core", ForgedAutomataCore::new);
    public static final RegistryObject<Item> SCIENTIFIC_AUTOMATA_CORE = Registration.ITEMS.register(
            "scientific_automata_core", () -> new PeripheralItem(() -> true, ScientificTurtle.ID, null));
    public static final RegistryObject<Item> ENCHANTING_AUTOMATA_CORE = Registration.ITEMS.register(
            "enchanting_automata_core", () -> new PeripheralItem(() -> AncientPeripheralsConfig.enableEnchantingAutomataCore, EnchantingTurtle.ID, null)
    );
    public static final RegistryObject<Item> SMITHING_AUTOMATA_CORE = Registration.ITEMS.register(
            "smithing_automata_core", () -> new PeripheralItem(() -> AncientPeripheralsConfig.enableSmithingAutomataCore, SmithingTurtle.ID, null)
    );

    // Abstractium
    public static final RegistryObject<Item> ABSTRACTIUM_INGOT = Registration.ITEMS.register(
            "abstractium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_SWORD = Registration.ITEMS.register(
            "abstractium_sword", () -> new SwordItem(ItemTier.ABSTRACTIUM, 3, -2.4F, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_SHOVEL = Registration.ITEMS.register(
            "abstractium_shovel", () -> new ShovelItem(ItemTier.ABSTRACTIUM, 1.5F, -3.0F, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_PICKAXE = Registration.ITEMS.register(
            "abstractium_pickaxe", () -> new PickaxeItem(ItemTier.ABSTRACTIUM, 1, -2.8F, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_AXE = Registration.ITEMS.register(
            "abstractium_axe", () -> new AxeItem(ItemTier.ABSTRACTIUM, 6.0F, -3.0F, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_HOE = Registration.ITEMS.register(
            "abstractium_hoe", () -> new HoeItem(ItemTier.ABSTRACTIUM, 0, -3.0F, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );

    public static final RegistryObject<Item> ABSTRACTIUM_HELMET = Registration.ITEMS.register(
            "abstractium_helmet", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.HEAD, (new Item.Properties()).tab(AncientPeripherals.TAB)));
    public static final RegistryObject<Item> ABSTRACTIUM_CHESTPLATE = Registration.ITEMS.register(
            "abstractium_chestplate", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.CHEST, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_LEGGINGS = Registration.ITEMS.register(
            "abstractium_leggings", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.LEGS, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );
    public static final RegistryObject<Item> ABSTRACTIUM_BOOTS = Registration.ITEMS.register(
            "abstractium_boots", () -> new ArmorItem(ArmorMaterial.ABSTRACTIUM, EquipmentSlotType.FEET, (new Item.Properties()).tab(AncientPeripherals.TAB))
    );

    public static void register() {
    }


}
