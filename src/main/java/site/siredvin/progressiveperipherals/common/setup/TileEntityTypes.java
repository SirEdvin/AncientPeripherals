package site.siredvin.progressiveperipherals.common.setup;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.tileentities.*;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.*;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.MachineryStorageTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileEntityTypes {

    @SuppressWarnings("EmptyMethod")
    static void register() {
    }

    @SafeVarargs
    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> ofBlock(Supplier<T> sup, RegistryObject<Block>... blocks) {
        return ofBlock(blocks[0].getId().getPath(), sup, blocks);
    }

    @SafeVarargs
    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> ofBlock(String name, Supplier<T> sup, RegistryObject<Block>... blocks) {
        //noinspection ConstantConditions
        return Registration.TILE_ENTITIES.register(
                name, () -> new TileEntityType<>(
                        sup, Sets.newHashSet(Arrays.stream(blocks).map(RegistryObject::get).collect(Collectors.toList())), null
                )
        );
    }

    // Peripherals

    public static final RegistryObject<TileEntityType<RealityForgerTileEntity>> REALITY_FORGER = ofBlock(RealityForgerTileEntity::new, Blocks.REALITY_FORGER, Blocks.REALITY_FORGER_MK2);

    public static final RegistryObject<TileEntityType<StatueWorkbenchTileEntity>> STATUE_WORKBENCH = ofBlock(StatueWorkbenchTileEntity::new, Blocks.STATUE_WORKBENCH);

    public static final RegistryObject<TileEntityType<AbstractiumPedestalTileEntity>> ABSTRACTIUM_PEDESTAL = ofBlock(AbstractiumPedestalTileEntity::new, Blocks.ABSTRACTIUM_PEDESTAL);

    public static final RegistryObject<TileEntityType<CreativeItemDuplicatorTileEntity>> CREATIVE_ITEM_DUPLICATOR = ofBlock(CreativeItemDuplicatorTileEntity::new, Blocks.CREATIVE_ITEM_DUPLICATOR);

    public static final RegistryObject<TileEntityType<RecipeRegistryTileEntity>> RECIPE_REGISTRY = ofBlock(RecipeRegistryTileEntity::new, Blocks.RECIPE_REGISTRY);

    public static final RegistryObject<TileEntityType<EventDistributorTileEntity>> EVENT_DISTRIBUTOR = ofBlock(EventDistributorTileEntity::new, Blocks.EVENT_DISTRIBUTOR);

    // Utility

    public static final RegistryObject<TileEntityType<FlexibleRealityAnchorTileEntity>> FLEXIBLE_REALITY_ANCHOR = ofBlock(FlexibleRealityAnchorTileEntity::new, Blocks.FLEXIBLE_REALITY_ANCHOR);

    public static final RegistryObject<TileEntityType<FlexibleStatueTileEntity>> FLEXIBLE_STATUE = ofBlock(FlexibleStatueTileEntity::new, Blocks.FLEXIBLE_STATUE);

    public static final RegistryObject<TileEntityType<RealityBreakthroughPointTileEntity>> REALITY_BREAKTHROUGH_POINT = ofBlock(RealityBreakthroughPointTileEntity::new, Blocks.REALITY_BREAKTHROUGH_POINT);

    public static final RegistryObject<TileEntityType<IrrealiumPedestalTileEntity>> IRREALIUM_PEDESTAL = ofBlock(IrrealiumPedestalTileEntity::new, Blocks.IRREALIUM_PEDESTAL);

    // Machinery
    public static final RegistryObject<TileEntityType<RBTRectorControllerTileEntity>> REALITY_BREAKTHROUGH_REACTOR_CONTROLLER = ofBlock(RBTRectorControllerTileEntity::new, Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER);

    public static final RegistryObject<TileEntityType<RBTExtractorControllerTileEntity>> REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER = ofBlock(RBTExtractorControllerTileEntity::new, Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER);

    public static final RegistryObject<TileEntityType<MachineryStorageTileEntity>> MACHINERY_STORAGE = ofBlock(MachineryStorageTileEntity::new, Blocks.IRREALIUM_MACHINERY_STORAGE, Blocks.IRREALIUM_DOUBLE_MACHINERY_STORAGE);

    // Enderwire network

    public static final RegistryObject<TileEntityType<EnderwireNetworkConnectorTileEntity>> ENDERWIRE_NETWORK_CONNECTOR = ofBlock(EnderwireNetworkConnectorTileEntity::new, Blocks.ENDERWIRE_NETWORK_CONNECTOR);

    public static final RegistryObject<TileEntityType<EnderwireNetworkAmplifierTileEntity>> ENDERWIRE_NETWORK_AMPLIFIER = ofBlock(EnderwireNetworkAmplifierTileEntity::new, Blocks.ENDERWIRE_NETWORK_AMPLIFIER);

    public static final RegistryObject<TileEntityType<EnderwireDimensionBreakerTileEntity>> ENDERWIRE_DIMENSION_BREAKER = ofBlock(EnderwireDimensionBreakerTileEntity::new, Blocks.ENDERWIRE_DIMENSION_BREAKER);


    public static final RegistryObject<TileEntityType<EnderwireSensorTileEntity>> ENDERWIRE_SENSOR = ofBlock(
            "enderwire/sensor", EnderwireSensorTileEntity::new,
            Blocks.ENDERWIRE_LEVER, Blocks.ADVANCED_ENDERWIRE_LEVER,
            Blocks.ENDERWIRE_BUTTON, Blocks.ADVANCED_ENDERWIRE_BUTTON,
            Blocks.ENDERWIRE_PRESSURE_PLATE, Blocks.ADVANCED_ENDERWIRE_PRESSURE_PLATE
    );

    public static final RegistryObject<TileEntityType<EnderwireRedstoneSensorTileEntity>> ENDERWIRE_REDSTONE_SENSOR = ofBlock(EnderwireRedstoneSensorTileEntity::new, Blocks.ENDERWIRE_REDSTONE_SENSOR);

    public static final RegistryObject<TileEntityType<EnderwireRedstoneEmitterTileEntity>> ENDERWIRE_REDSTONE_EMITTER = ofBlock(EnderwireRedstoneEmitterTileEntity::new, Blocks.ENDERWIRE_REDSTONE_EMITTER);

    public static final RegistryObject<TileEntityType<EnderwireLightEmitterTileEntity>> ENDERWIRE_LIGHT_EMITTER = ofBlock(EnderwireLightEmitterTileEntity::new, Blocks.ENDERWIRE_LIGHT_EMITTER);

    public static final RegistryObject<TileEntityType<EnderwirePeripheralSharingTileEntity>> ENDERWIRE_PERIPHERAL_SHARING = ofBlock(EnderwirePeripheralSharingTileEntity::new, Blocks.ENDERWIRE_PERIPHERAL_SHARING);

    public static final RegistryObject<TileEntityType<EnderwireModemTileEntity>> ENDERWIRE_MODEM = ofBlock(EnderwireModemTileEntity::new, Blocks.ENDERWIRE_MODEM);
}