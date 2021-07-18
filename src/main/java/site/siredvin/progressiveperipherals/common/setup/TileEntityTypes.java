package site.siredvin.progressiveperipherals.common.setup;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.tileentities.HIVEControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.RealityForgerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleRealityAnchorTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.StatueWorkbenchTileEntity;

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
    public static final RegistryObject<TileEntityType<HIVEControllerTileEntity>> HIVE_CONTROLLER = Registration.TILE_ENTITIES.register(
            "hive_controller",
            () -> new TileEntityType<>(HIVEControllerTileEntity::new, Sets.newHashSet(Blocks.HIVE_CONTROLLER.get()), null)
    );
}