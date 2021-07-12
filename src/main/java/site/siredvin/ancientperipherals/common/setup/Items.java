package site.siredvin.ancientperipherals.common.setup;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.ancientperipherals.common.items.BaseItem;
import site.siredvin.ancientperipherals.common.items.ForgedMechanicSoul;

public class Items {

    public static final RegistryObject<Item> FORGED_MECHANIC_SOUL = Registration.ITEMS.register("forged_mechanic_soul", ForgedMechanicSoul::new);
    public static final RegistryObject<Item> TRAINABLE_MECHANIC_SOUL = Registration.ITEMS.register("trainable_mechanic_soul", BaseItem::new);

    public static void register() {
    }


}
