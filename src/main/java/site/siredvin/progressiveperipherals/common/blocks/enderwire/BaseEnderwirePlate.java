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
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;
import site.siredvin.progressiveperipherals.utils.BlockUtils;
import site.siredvin.progressiveperipherals.utils.ShapeUtils;

import static site.siredvin.progressiveperipherals.common.blocks.enderwire.BaseEnderwireBlock.CONNECTED;

public abstract class BaseEnderwirePlate<T extends TileEntity> extends HorizontalFaceBlock {

    private static final VoxelShape DOWN_ATTACHED = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape UP_ATTACHED = Block.box(0, 14, 0, 16, 16, 16);
    private static final VoxelShape NORTH_ATTACHED = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.SOUTH, NORTH_ATTACHED);
    private static final VoxelShape EAST_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.EAST, NORTH_ATTACHED);
    private static final VoxelShape WEST_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.WEST, NORTH_ATTACHED);

    public BaseEnderwirePlate() {
        super(BlockUtils.decoration());
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED, FACE, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACE)) {
            case FLOOR:
                return DOWN_ATTACHED;
            case CEILING:
                return UP_ATTACHED;
        }
        switch (state.getValue(FACING)) {
            case NORTH:
                return SOUTH_ATTACHED;
            case SOUTH:
                return NORTH_ATTACHED;
            case EAST:
                return WEST_ATTACHED;
            case WEST:
                return EAST_ATTACHED;
        }
        return DOWN_ATTACHED;
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
        if (!newState.is(this)) // new block are not this block
            NetworkElementTool.handleRemove(world, pos);
        super.onRemove(state, world, pos, newState, isMoving);
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

    public abstract T newTileEntity(BlockState state, IBlockReader world);
}
