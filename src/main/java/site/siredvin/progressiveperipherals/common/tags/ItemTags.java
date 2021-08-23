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

    public static final ITag.INamedTag<Item> IRREALIUM_STRUCTURE_CASING = bind("irrealium_structure_casing");
    public static final ITag.INamedTag<Item> IRREALIUM_STRUCTURE_CORNER = bind("irrealium_structure_corner");

    public static ITag.INamedTag<Item> bind(String p_199894_0_) {
        return HELPER.bind(ProgressivePeripherals.MOD_ID + ":" + p_199894_0_);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register(){}
}
