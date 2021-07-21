package site.siredvin.progressiveperipherals.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

public class BlockTags {
    protected static final TagRegistry<Block> HELPER = TagRegistryManager.create(new ResourceLocation(ProgressivePeripherals.MOD_ID, "block"), ITagCollectionSupplier::getBlocks);

    public static final ITag.INamedTag<Block> BREAKTHROUGH_REACTOR_CASING = bind("breakthrough_reactor_casing");
    public static final ITag.INamedTag<Block> BREAKTHROUGH_REACTOR_CORNER = bind("breakthrough_reactor_corner");

    public static ITag.INamedTag<Block> bind(String p_199894_0_) {
        return HELPER.bind(ProgressivePeripherals.MOD_ID + ":" + p_199894_0_);
    }

    public static void register(){}
}
