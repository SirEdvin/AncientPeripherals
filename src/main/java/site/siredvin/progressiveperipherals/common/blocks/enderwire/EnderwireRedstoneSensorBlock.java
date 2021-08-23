package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireRedstoneSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEventProducer;

import java.util.Objects;

public class EnderwireRedstoneSensorBlock extends BaseEnderwirePlate<EnderwireRedstoneSensorTileEntity> {

    public EnderwireRedstoneSensorBlock() {
        super();
    }

    @Override
    public @NotNull EnderwireRedstoneSensorTileEntity newTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireRedstoneSensorTileEntity();
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, World world, @NotNull BlockPos pos, @NotNull Block neighbor, @NotNull BlockPos neighborPos, boolean p_220069_6_) {
        super.neighborChanged(state, world, pos, neighbor, neighborPos, p_220069_6_);
        if (!world.isClientSide) {
            Vector3i neighborDiff = pos.subtract(neighborPos);
            Direction neighborDirection = Direction.fromNormal(neighborDiff.getX(), neighborDiff.getY(), neighborDiff.getZ());
            Objects.requireNonNull(neighborDirection);
            BlockState neighborState = world.getBlockState(neighborPos);
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof EnderwireRedstoneSensorTileEntity) {
                EnderwireRedstoneSensorTileEntity sensorTE = (EnderwireRedstoneSensorTileEntity) te;
                int signal = neighborState.getSignal(world, neighborPos, neighborDirection.getOpposite());
                if (sensorTE.getPower(neighborDirection) != signal) {
                    sensorTE.setPower(neighborDirection, signal);
                    EnderwireNetworkEventProducer.fireRedstoneSensorEvent(signal, neighborDirection, world, pos);
                }

            }
        }
    }
}
