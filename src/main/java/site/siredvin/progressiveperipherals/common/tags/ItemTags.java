package site.siredvin.progressiveperipherals.common.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagRegistry;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.ResourceLocation;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

public class ItemTags {
    protected static final TagRegistry<Item> HELPER = TagRegistryManager.create(new ResourceLocation(ProgressivePeripherals.MOD_ID, "item"), ITagCollectionSupplier::getItems);

    public static final ITag.INamedTag<Item> BREAKTHROUGH_REACTOR_CASING = bind("breakthrough_reactor_casing");

    public static ITag.INamedTag<Item> bind(String p_199894_0_) {
        return HELPER.bind(ProgressivePeripherals.MOD_ID + ":" + p_199894_0_);
    }
}
