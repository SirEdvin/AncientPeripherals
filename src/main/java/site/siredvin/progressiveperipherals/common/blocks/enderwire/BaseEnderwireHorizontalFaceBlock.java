package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.IBlockObservingTileEntity;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import javax.annotation.Nonnull;
import java.util.Random;

import static site.siredvin.progressiveperipherals.common.blocks.enderwire.BaseEnderwireBlock.CONNECTED;

@SuppressWarnings("ALL")
public abstract class BaseEnderwireHorizontalFaceBlock<T extends TileEntity> extends HorizontalFaceBlock {

    public BaseEnderwireHorizontalFaceBlock() {
        super(BlockUtils.decoration());
        this.registerDefaultState(generateDefaultState(this.getStateDefinition().any()));
    }

    public BlockState generateDefaultState(BlockState parentDefaultState) {
        return parentDefaultState.setValue(CONNECTED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED, FACE, FACING);
    }

    @Override
    public void setPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            NetworkElementTool.handleNetworkSetup(Hand.OFF_HAND, (PlayerEntity) entity, (ServerWorld) world, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean isBlockRemoved = !newState.is(this);
        if (isBlockRemoved) // new block are not this block
            NetworkElementTool.handleRemove(world, pos);
        TileEntity tile = world.getBlockEntity(pos);
        super.onRemove(state, world, pos, newState, isMoving);
        if (isBlockRemoved && tile instanceof IBlockObservingTileEntity)
            ((IBlockObservingTileEntity) tile).destroy();
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState newState, boolean bool) {
        super.onPlace(state, world, pos, newState, bool);
        if (newState.getBlock() == this) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof IBlockObservingTileEntity)
                ((IBlockObservingTileEntity) te).placed();
        }
    }

    @Override
    public @NotNull ActionResultType use(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity playerEntity, @NotNull Hand hand, @NotNull BlockRayTraceResult hit) {
        ActionResultType handledUse = NetworkElementTool.handleUse(state, world, pos, playerEntity, hand, hit);
        if (handledUse != null)
            return handledUse;
        return super.use(state, world, pos, playerEntity, hand, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return newTileEntity(state, world);
    }

    @SuppressWarnings("unused")
    public abstract T newTileEntity(BlockState state, IBlockReader world);

    @Override
    @Deprecated
    public void neighborChanged(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull Block neighbourBlock, @Nonnull BlockPos neighbourPos, boolean isMoving) {
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
    public void tick(@Nonnull BlockState state, ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        super.tick(state, world, pos, rand);
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof IBlockObservingTileEntity)
            ((IBlockObservingTileEntity) te).blockTick();
    }
}
