package site.siredvin.progressiveperipherals.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public abstract class BasePedestal<T extends TileEntity> extends TileEntityBlock<T> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape PEDESTAL = Stream.of(
            Block.box(3, 0, 3, 13, 1, 13),
            Block.box(6, 1, 6, 10, 9, 10),
            Block.box(4, 9, 4, 12, 10, 12),
            Block.box(5, 10, 5, 11, 11, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape DOWN_PEDESTAL = Stream.of(
            Block.box(3, 15, 3, 13, 16, 13),
            Block.box(6, 7, 6, 10, 15, 10),
            Block.box(4, 6, 4, 12, 7, 12),
            Block.box(5, 5, 5, 11, 6, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SOUTH_PEDESTAL = Stream.of(
            Block.box(3, 3, 0, 13, 13, 1),
            Block.box(6, 6, 1, 10, 10, 9),
            Block.box(4, 4, 9, 12, 12, 10),
            Block.box(5, 5, 10, 11, 11, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape NORTH_PEDESTAL = Stream.of(
            Block.box(3, 3, 15, 13, 13, 16),
            Block.box(6, 6, 7, 10, 10, 15),
            Block.box(4, 4, 6, 12, 12, 7),
            Block.box(5, 5, 5, 11, 11, 6)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape WEST_PEDESTAL = Stream.of(
            Block.box(15, 3, 3, 16, 13, 13),
            Block.box(7, 6, 6, 15, 10, 10),
            Block.box(6, 4, 4, 7, 12, 12),
            Block.box(5, 5, 5, 6, 11, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape EAST_PEDESTAL = Stream.of(
            Block.box(0, 3, 3, 1, 13, 13),
            Block.box(1, 6, 6, 9, 10, 10),
            Block.box(9, 4, 4, 10, 12, 12),
            Block.box(10, 5, 5, 11, 11, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public BasePedestal(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
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
        switch(state.getValue(FACING)) {
            case SOUTH:
                return SOUTH_PEDESTAL;
            case NORTH:
                return NORTH_PEDESTAL;
            case EAST:
                return EAST_PEDESTAL;
            case WEST:
                return WEST_PEDESTAL;
            case DOWN:
                return DOWN_PEDESTAL;
        }
        return PEDESTAL;
    }
}
