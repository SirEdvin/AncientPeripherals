package site.siredvin.progressiveperipherals.common.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.blocks.*;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseBlock;
import site.siredvin.progressiveperipherals.common.blocks.multiblock.MultiBlockBlock;
import site.siredvin.progressiveperipherals.common.blocks.multiblock.RealityBreakthroughReactorController;
import site.siredvin.progressiveperipherals.common.blocks.multiblock.MultiBlockGlass;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.items.FlexibleRealityAnchorItem;
import site.siredvin.progressiveperipherals.common.items.FlexibleStatueItem;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;
import site.siredvin.progressiveperipherals.common.items.peripheral.PeripheralBlockItem;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import java.util.function.Supplier;

public class Blocks {

    static void register() {
    }

    // Peripherals
    public static final RegistryObject<Block> REALITY_FORGER = register(
            "reality_forger",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.REALITY_FORGER),
            () -> new PeripheralBlockItem(Blocks.REALITY_FORGER.get(), null, null, () -> ProgressivePeripheralsConfig.enableRealityForger)
    );

    public static final RegistryObject<Block> STATUE_WORKBENCH = register(
            "statue_workbench",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.STATUE_WORKBENCH),
            () -> new PeripheralBlockItem(Blocks.STATUE_WORKBENCH.get(), null, null, () -> ProgressivePeripheralsConfig.enableStatueWorkbench)
    );

    public static final RegistryObject<Block> ABSTRACTIUM_PEDESTAL = register(
            "abstractium_pedestal",
            AbstractiumPedestal::new,
            () -> new PeripheralBlockItem(Blocks.ABSTRACTIUM_PEDESTAL.get(), null, null, () -> true)
    );

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_REACTOR_CONTROLLER = register(
            "reality_breakthrough_reactor_controller",
            RealityBreakthroughReactorController::new,
            () -> new PeripheralBlockItem(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get(), null, null, () -> true)
    );


    // Utility

    public static final RegistryObject<Block> FLEXIBLE_REALITY_ANCHOR = register(
            "flexible_reality_anchor", FlexibleRealityAnchor::new, FlexibleRealityAnchorItem::new
    );

    public static final RegistryObject<Block> FLEXIBLE_STATUE = register(
            "flexible_statue", FlexibleStatue::new, FlexibleStatueItem::new
    );

    public static final RegistryObject<Block> IRREALIUM_PEDESTAL = register(
            "irrealium_pedestal",
            IrrealiumPedestal::new,
            () -> new BaseBlockItem(Blocks.IRREALIUM_PEDESTAL.get())
    );

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_REACTOR_CASING = register(
            "reality_breakthrough_reactor_casing",
            () -> new MultiBlockBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.REALITY_BREAKTHROUGH_REACTOR_CASING.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_REACTOR_GLASS = register(
            "reality_breakthrough_reactor_glass",
            () -> new MultiBlockGlass(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.REALITY_BREAKTHROUGH_REACTOR_GLASS.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_REACTOR_IO_PORT = register(
            "reality_breakthrough_reactor_io_port",
            () -> new MultiBlockBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.REALITY_BREAKTHROUGH_REACTOR_IO_PORT.get(), new Item.Properties().stacksTo(64))
    );


    // World feature

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_POINT = register(
            "reality_breakthrough_point",
            RealityBreakthroughPoint::new,
            () -> new BaseBlockItem(Blocks.REALITY_BREAKTHROUGH_POINT.get())
    );

    // Other
    public static final RegistryObject<Block> ABSTRACTIUM_BLOCK = register(
            "abstractium_block",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.ABSTRACTIUM_BLOCK.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_BLOCK = register(
            "irrealium_block",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_BLOCK.get(), new Item.Properties().stacksTo(64))
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