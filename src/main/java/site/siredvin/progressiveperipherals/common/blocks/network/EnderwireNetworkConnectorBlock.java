package site.siredvin.progressiveperipherals.common.blocks.network;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.network.NetworkConnectorTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

public class EnderwireNetworkConnectorBlock extends BaseEnderwireBlock<NetworkConnectorTileEntity> {
    public EnderwireNetworkConnectorBlock() {
        super(BlockUtils.defaultProperties());
    }

    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NetworkConnectorTileEntity();
    }
}
