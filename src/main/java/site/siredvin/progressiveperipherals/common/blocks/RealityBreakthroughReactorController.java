package site.siredvin.progressiveperipherals.common.blocks;

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
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class RealityBreakthroughReactorController extends GenericTileEntityBlock<RealityBreakthroughRectorControllerTileEntity> {
    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RealityBreakthroughReactorController() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
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
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
