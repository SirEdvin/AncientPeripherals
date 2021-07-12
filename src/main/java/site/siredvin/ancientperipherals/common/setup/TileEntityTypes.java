package site.siredvin.ancientperipherals.common.setup;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.ancientperipherals.common.tileentities.RealityForgerTileEntity;
import site.siredvin.ancientperipherals.common.tileentities.FlexibleRealityAnchorTileEntity;

public class TileEntityTypes {

    static void register() {
    }

    public static final RegistryObject<TileEntityType<FlexibleRealityAnchorTileEntity>> FLEXIBLE_REALITY_ANCHOR = Registration.TILE_ENTITIES.register("flexible_reality_anchor", () -> new TileEntityType<>(FlexibleRealityAnchorTileEntity::new, Sets.newHashSet(Blocks.FLEXIBLE_REALITY_ANCHOR.get()), null));
    public static final RegistryObject<TileEntityType<RealityForgerTileEntity>> REALITY_FORGER = Registration.TILE_ENTITIES.register("reality_forger", () -> new TileEntityType<>(RealityForgerTileEntity::new, Sets.newHashSet(Blocks.REALITY_FORGER.get()), null));
}