package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireRedstoneSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkProducer;
import site.siredvin.progressiveperipherals.utils.BlockUtils;
import site.siredvin.progressiveperipherals.utils.ShapeUtils;

import java.util.Objects;

public class EnderwireRedstoneSensorBlock extends BaseEnderwireBlock<EnderwireRedstoneSensorTileEntity> {

    private static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape DOWN_ATTACHED = Block.box(5, 0, 5, 11, 2, 11);
    private static final VoxelShape UP_ATTACHED = Block.box(5, 14, 5, 11, 16, 11);
    private static final VoxelShape NORTH_ATTACHED = Block.box(5, 5, 0, 11, 11, 2);
    private static final VoxelShape SOUTH_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.SOUTH, NORTH_ATTACHED);
    private static final VoxelShape EAST_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.EAST, NORTH_ATTACHED);
    private static final VoxelShape WEST_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.WEST, NORTH_ATTACHED);

    public EnderwireRedstoneSensorBlock() {
        super(BlockUtils.decoration());
    }

    @Override
    public @NotNull EnderwireRedstoneSensorTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireRedstoneSensorTileEntity();
    }

    @Override
    public BlockState generateDefaultState(BlockState parentDefaultState) {
        return super.generateDefaultState(parentDefaultState).setValue(FACING, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean p_220069_6_) {
        super.neighborChanged(state, world, pos, neighbor, neighborPos, p_220069_6_);
        Vector3i neighborDiff = pos.subtract(neighborPos);
        Direction neighborDirection = Direction.fromNormal(neighborDiff.getX(), neighborDiff.getY(), neighborDiff.getZ());
        Objects.requireNonNull(neighborDirection);
        BlockState neighborState = world.getBlockState(neighborPos);
        EnderwireNetworkProducer.fireRedstoneSensorEvent(
                neighborState.getSignal(world, neighborPos, neighborDirection.getOpposite()),
                neighborDirection,
                world, pos
        );
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING)) {
            case UP:
                return DOWN_ATTACHED;
            case DOWN:
                return UP_ATTACHED;
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
}
