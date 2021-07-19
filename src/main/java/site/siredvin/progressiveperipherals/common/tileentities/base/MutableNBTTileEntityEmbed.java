package site.siredvin.progressiveperipherals.common.tileentities.base;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.common.util.Constants;

public class MutableNBTTileEntityEmbed {

    public static void pushState(TileEntity tileEntity, BlockState state) {
        World world = tileEntity.getLevel();
        if (world != null) {
            if (world.isClientSide) {
                ModelDataManager.requestModelDataRefresh(tileEntity);
            } else {
                tileEntity.setChanged();
                world.setBlockAndUpdate(tileEntity.getBlockPos(), state);
                world.sendBlockUpdated(
                        tileEntity.getBlockPos(), tileEntity.getBlockState(), tileEntity.getBlockState(),
                        Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
                );
            }
        }
    }
}
