package site.siredvin.ancientperipherals.common.setup;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.ancientperipherals.common.items.BaseItem;
import site.siredvin.ancientperipherals.common.items.ForgedAutomataCore;

public class Items {

    public static final RegistryObject<Item> FORGED_AUTOMATA_CORE = Registration.ITEMS.register("forged_automata_core", ForgedAutomataCore::new);
    public static final RegistryObject<Item> SCIENTIFIC_AUTOMATA_CORE = Registration.ITEMS.register("scientific_automata_core", BaseItem::new);
    public static final RegistryObject<Item> ABSTRACTIUM_INGOT = Registration.ITEMS.register("abstractium_ingot", () -> new BaseItem(new Item.Properties().stacksTo(64)));

    public static void register() {
    }


}
