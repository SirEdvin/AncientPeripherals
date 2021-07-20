package site.siredvin.progressiveperipherals.common.setup;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.tileentities.*;

public class TileEntityTypes {

    static void register() {
    }

    public static final RegistryObject<TileEntityType<FlexibleRealityAnchorTileEntity>> FLEXIBLE_REALITY_ANCHOR = Registration.TILE_ENTITIES.register(
            "flexible_reality_anchor",
            () -> new TileEntityType<>(FlexibleRealityAnchorTileEntity::new, Sets.newHashSet(Blocks.FLEXIBLE_REALITY_ANCHOR.get()),null)
    );
    public static final RegistryObject<TileEntityType<RealityForgerTileEntity>> REALITY_FORGER = Registration.TILE_ENTITIES.register(
            "reality_forger",
            () -> new TileEntityType<>(RealityForgerTileEntity::new, Sets.newHashSet(Blocks.REALITY_FORGER.get()), null)
    );
    public static final RegistryObject<TileEntityType<FlexibleStatueTileEntity>> FLEXIBLE_STATUE = Registration.TILE_ENTITIES.register(
            "flexible_statue",
            () -> new TileEntityType<>(FlexibleStatueTileEntity::new, Sets.newHashSet(Blocks.FLEXIBLE_STATUE.get()), null)
    );
    public static final RegistryObject<TileEntityType<StatueWorkbenchTileEntity>> STATUE_WORKBENCH = Registration.TILE_ENTITIES.register(
            "statue_workbench",
            () -> new TileEntityType<>(StatueWorkbenchTileEntity::new, Sets.newHashSet(Blocks.STATUE_WORKBENCH.get()), null)
    );
    public static final RegistryObject<TileEntityType<RealityBreakthroughPointTileEntity>> REALITY_BREAKTHROUGH_POINT = Registration.TILE_ENTITIES.register(
            "reality_breakthrough_point",
            () -> new TileEntityType<>(RealityBreakthroughPointTileEntity::new, Sets.newHashSet(Blocks.REALITY_BREAKTHROUGH_POINT.get()), null)
    );
    public static final RegistryObject<TileEntityType<AbstractiumPedestalTileEntity>> ABSTRACTIUM_PEDESTAL = Registration.TILE_ENTITIES.register(
            "abstractium_pedestal",
            () -> new TileEntityType<>(AbstractiumPedestalTileEntity::new, Sets.newHashSet(Blocks.ABSTRACTIUM_PEDESTAL.get()), null)
    );
    public static final RegistryObject<TileEntityType<IrrealiumPedestalTileEntity>> IRRELIUM_PEDESTAL = Registration.TILE_ENTITIES.register(
            "irrelium_pedestal",
            () -> new TileEntityType<>(IrrealiumPedestalTileEntity::new, Sets.newHashSet(Blocks.IRREALIUM_PEDESTAL.get()), null)
    );
    public static final RegistryObject<TileEntityType<RealityBreakthroughRectorControllerTileEntity>> REALITY_BREAKTHROUGH_REACTOR_CONTROLLER = Registration.TILE_ENTITIES.register(
            "reality_breakthrough_reactor_controller",
            () -> new TileEntityType<>(RealityBreakthroughRectorControllerTileEntity::new, Sets.newHashSet(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get()), null)
    );
}