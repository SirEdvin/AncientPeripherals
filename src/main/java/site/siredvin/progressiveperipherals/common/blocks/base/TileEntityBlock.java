package site.siredvin.progressiveperipherals.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.tileentity.IBlockObservingTileEntity;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class TileEntityBlock<T extends TileEntity> extends BaseBlock {
    public TileEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public abstract @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    @Deprecated
    public void neighborChanged(@Nonnull BlockState state, @NotNull World world, @Nonnull BlockPos pos, @Nonnull Block neighbourBlock, @Nonnull BlockPos neighbourPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, neighbourBlock, neighbourPos, isMoving);
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IBlockObservingTileEntity)
            ((IBlockObservingTileEntity) tile).onNeighbourChange(neighbourPos);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbour) {
        super.onNeighborChange(state, world, pos, neighbour);
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IBlockObservingTileEntity)
            ((IBlockObservingTileEntity) tile).onNeighbourTileEntityChange(neighbour);
    }

    @Override
    @Deprecated
    public void tick(@Nonnull BlockState state, @NotNull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        super.tick(state, world, pos, rand);
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof IBlockObservingTileEntity)
            ((IBlockObservingTileEntity) te).blockTick();
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean bool) {
        super.onPlace(state, world, pos, newState, bool);
        if (newState.getBlock() == this) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof IBlockObservingTileEntity)
                ((IBlockObservingTileEntity) te).placed();
        }
    }

    @Override
    @Deprecated
    public void onRemove(@Nonnull BlockState block, @Nonnull World world, @Nonnull BlockPos pos, BlockState replace, boolean bool) {
        if (block.getBlock() == replace.getBlock())
            return;

        TileEntity tile = world.getBlockEntity(pos);
        super.onRemove(block, world, pos, replace, bool);
        if (tile instanceof IBlockObservingTileEntity) ((IBlockObservingTileEntity) tile).destroy();
    }
}
