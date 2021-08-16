package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.Items;

public enum RealityBreakthroughPointType {
    IRREALIUM(Items.IRREALIUM_INGOT);

    private final @Nullable RegistryObject<Item> producibleItem;

    @SuppressWarnings("SameParameterValue")
    RealityBreakthroughPointType(@Nullable RegistryObject<Item> producibleItem) {
        this.producibleItem = producibleItem;
    }

    public @Nullable Item getProducibleItem() {
        if (producibleItem != null)
            return producibleItem.get();
        return null;
    }
}
