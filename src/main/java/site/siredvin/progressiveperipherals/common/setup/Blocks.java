package site.siredvin.progressiveperipherals.common.setup;

import de.srendi.advancedperipherals.common.blocks.base.APTileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.blocks.BaseBlock;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;
import site.siredvin.progressiveperipherals.common.items.peripheral.PeripheralBlockItem;

import java.util.function.Supplier;

public class Blocks {

    static void register() {
    }

    public static final RegistryObject<Block> FLEXIBLE_REALITY_ANCHOR = register(
            "flexible_reality_anchor", FlexibleRealityAnchor::new, () -> new BaseBlockItem(Blocks.FLEXIBLE_REALITY_ANCHOR.get())
    );
    public static final RegistryObject<Block> REALITY_FORGER = register(
            "reality_forger",
            () -> new APTileEntityBlock<>(TileEntityTypes.REALITY_FORGER, true),
            () -> new PeripheralBlockItem(Blocks.REALITY_FORGER.get(), null, null, () -> ProgressivePeripheralsConfig.enableRealityForger)
    );
    public static final RegistryObject<Block> ABSTRACTIUM_BLOCK = register(
            "abstractium_block",
            BaseBlock::new,
            () -> new BaseBlockItem(Blocks.ABSTRACTIUM_BLOCK.get())
    );

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return Registration.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Supplier<BlockItem> blockItem) {
        RegistryObject<T> registryObject = registerNoItem(name, block);
        Registration.ITEMS.register(name, blockItem);
        return registryObject;
    }

}