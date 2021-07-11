package site.siredvin.ancientperipherals.common.setup;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.ancientperipherals.common.items.BaseItem;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class Items {

    public static final RegistryObject<Item> FORGED_MECHANIC_SOUL = Registration.ITEMS.register("forged_mechanic_soul", () -> new BaseItem(new Item.Properties().stacksTo(1)));

    public static void register() {
    }


}
