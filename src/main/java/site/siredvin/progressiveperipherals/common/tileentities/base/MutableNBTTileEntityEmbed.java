package site.siredvin.progressiveperipherals.common.tileentities.base;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class MutableNBTTileEntityEmbed {

    public static void pushState(TileEntity tileEntity, BlockState state) {
        World world = tileEntity.getLevel();
        if (world != null) {
            if (world.isClientSide) {
                tileEntity.requestModelDataUpdate();
                BlockPos pos = tileEntity.getBlockPos();
                // Basically, just world.setBlocksDirty with bypass model block state check
                Minecraft.getInstance().levelRenderer.setBlocksDirty(
                        pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ()
                );
            } else {
                tileEntity.setChanged();
                world.setBlockAndUpdate(tileEntity.getBlockPos(), state);
                world.sendBlockUpdated(
                        tileEntity.getBlockPos(), tileEntity.getBlockState(), tileEntity.getBlockState(),
                        Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS
                );
            }
        }
    }
}
