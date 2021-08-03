package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireRedstoneEmitterTileEntity;

public class EnderwireRedstoneEmitterBlock extends BaseEnderwirePlate<EnderwireRedstoneEmitterTileEntity> {

    public EnderwireRedstoneEmitterBlock() {
        super();
    }

    @Override
    public int getSignal(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
        EnderwireRedstoneEmitterTileEntity te = (EnderwireRedstoneEmitterTileEntity) world.getBlockEntity(pos);
        if (te != null)
            return te.getPower(direction);
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public @NotNull EnderwireRedstoneEmitterTileEntity newTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireRedstoneEmitterTileEntity();
    }
}
