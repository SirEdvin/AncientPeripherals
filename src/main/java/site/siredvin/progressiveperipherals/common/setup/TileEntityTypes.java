package site.siredvin.progressiveperipherals.common.setup;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.tileentities.*;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireConnectorTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireSensorTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.MachineryStorageTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;

public class TileEntityTypes {

    static void register() {
    }

    // Peripherals

    public static final RegistryObject<TileEntityType<RealityForgerTileEntity>> REALITY_FORGER = Registration.TILE_ENTITIES.register(
            "reality_forger",
            () -> new TileEntityType<>(
                    RealityForgerTileEntity::new,
                    Sets.newHashSet(Blocks.REALITY_FORGER.get(), Blocks.REALITY_FORGER_MK2.get()),
                    null
            )
    );

    public static final RegistryObject<TileEntityType<StatueWorkbenchTileEntity>> STATUE_WORKBENCH = Registration.TILE_ENTITIES.register(
            "statue_workbench",
            () -> new TileEntityType<>(StatueWorkbenchTileEntity::new, Sets.newHashSet(Blocks.STATUE_WORKBENCH.get()), null)
    );

    public static final RegistryObject<TileEntityType<AbstractiumPedestalTileEntity>> ABSTRACTIUM_PEDESTAL = Registration.TILE_ENTITIES.register(
            "abstractium_pedestal",
            () -> new TileEntityType<>(AbstractiumPedestalTileEntity::new, Sets.newHashSet(Blocks.ABSTRACTIUM_PEDESTAL.get()), null)
    );

    public static final RegistryObject<TileEntityType<CreativeItemDuplicatorTileEntity>> CREATIVE_ITEM_DUPLICATOR = Registration.TILE_ENTITIES.register(
            "creative_item_duplicator",
            () -> new TileEntityType<>(CreativeItemDuplicatorTileEntity::new, Sets.newHashSet(Blocks.CREATIVE_ITEM_DUPLICATOR.get()), null)
    );

    public static final RegistryObject<TileEntityType<RecipeRegistryTileEntity>> RECIPE_REGISTRY = Registration.TILE_ENTITIES.register(
            "recipe_registry",
            () -> new TileEntityType<>(RecipeRegistryTileEntity::new, Sets.newHashSet(Blocks.RECIPE_REGISTRY.get()), null)
    );

    public static final RegistryObject<TileEntityType<EventDistributorTileEntity>> EVENT_DISTRIBUTOR = Registration.TILE_ENTITIES.register(
            "event_distributor",
            () -> new TileEntityType<>(EventDistributorTileEntity::new, Sets.newHashSet(Blocks.EVENT_DISTRIBUTOR.get()), null)
    );

    // Utility

    public static final RegistryObject<TileEntityType<FlexibleRealityAnchorTileEntity>> FLEXIBLE_REALITY_ANCHOR = Registration.TILE_ENTITIES.register(
            "flexible_reality_anchor",
            () -> new TileEntityType<>(FlexibleRealityAnchorTileEntity::new, Sets.newHashSet(Blocks.FLEXIBLE_REALITY_ANCHOR.get()),null)
    );

    public static final RegistryObject<TileEntityType<FlexibleStatueTileEntity>> FLEXIBLE_STATUE = Registration.TILE_ENTITIES.register(
            "flexible_statue",
            () -> new TileEntityType<>(FlexibleStatueTileEntity::new, Sets.newHashSet(Blocks.FLEXIBLE_STATUE.get()), null)
    );

    public static final RegistryObject<TileEntityType<RealityBreakthroughPointTileEntity>> REALITY_BREAKTHROUGH_POINT = Registration.TILE_ENTITIES.register(
            "reality_breakthrough_point",
            () -> new TileEntityType<>(RealityBreakthroughPointTileEntity::new, Sets.newHashSet(Blocks.REALITY_BREAKTHROUGH_POINT.get()), null)
    );

    public static final RegistryObject<TileEntityType<IrrealiumPedestalTileEntity>> IRREALIUM_PEDESTAL = Registration.TILE_ENTITIES.register(
            "irrealium_pedestal",
            () -> new TileEntityType<>(IrrealiumPedestalTileEntity::new, Sets.newHashSet(Blocks.IRREALIUM_PEDESTAL.get()), null)
    );

    // Machinery
    public static final RegistryObject<TileEntityType<RBTRectorControllerTileEntity>> REALITY_BREAKTHROUGH_REACTOR_CONTROLLER = Registration.TILE_ENTITIES.register(
            "reality_breakthrough_reactor_controller",
            () -> new TileEntityType<>(RBTRectorControllerTileEntity::new, Sets.newHashSet(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get()), null)
    );
    public static final RegistryObject<TileEntityType<RBTExtractorControllerTileEntity>> REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER = Registration.TILE_ENTITIES.register(
            "reality_breakthrough_reactor_extractor",
            () -> new TileEntityType<>(RBTExtractorControllerTileEntity::new, Sets.newHashSet(Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get()), null)
    );
    public static final RegistryObject<TileEntityType<MachineryStorageTileEntity>> MACHINERY_STORAGE = Registration.TILE_ENTITIES.register(
            "machinery_storage",
            () -> new TileEntityType<>(MachineryStorageTileEntity::new, Sets.newHashSet(Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get()), null)
    );

    // Enderwire network

    public static final RegistryObject<TileEntityType<EnderwireConnectorTileEntity>> ENDERWIRE_NETWORK_CONNECTOR = Registration.TILE_ENTITIES.register(
            "enderwire_network_connector",
            () -> new TileEntityType<>(EnderwireConnectorTileEntity::new, Sets.newHashSet(Blocks.ENDERWIRE_NETWORK_CONNECTOR.get()), null)
    );

    public static final RegistryObject<TileEntityType<EnderwireSensorTileEntity>> ENDERWIRE_SENSOR = Registration.TILE_ENTITIES.register(
            "enderwire_sensor",
            () -> new TileEntityType<>(
                    EnderwireSensorTileEntity::new,
                    Sets.newHashSet(Blocks.ENDERWIRE_LEVER.get(), Blocks.ENDERWIRE_BUTTON.get(), Blocks.ENDERWIRE_PRESSURE_PLATE.get()),
                    null
            )
    );
}