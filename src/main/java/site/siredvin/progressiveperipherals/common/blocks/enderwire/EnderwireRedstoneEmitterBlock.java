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
    public int getSignal(@NotNull BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos, @NotNull Direction direction) {
        return getDirectSignal(state, world, pos, direction);
    }

    @Override
    public int getDirectSignal(@NotNull BlockState state, IBlockReader world, @NotNull BlockPos pos, @NotNull Direction direction) {
        EnderwireRedstoneEmitterTileEntity te = (EnderwireRedstoneEmitterTileEntity) world.getBlockEntity(pos);
        if (te != null)
            return te.getPower(direction.getOpposite());
        return 0;
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState p_149744_1_) {
        return true;
    }

    @Override
    public @NotNull EnderwireRedstoneEmitterTileEntity newTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireRedstoneEmitterTileEntity();
    }
}
