package site.siredvin.progressiveperipherals.common.blocks.rbtreactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.GenericTileEntityBlock;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockProperties;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class RealityBreakthroughReactorController extends GenericTileEntityBlock<RealityBreakthroughRectorControllerTileEntity> {
    public static final BooleanProperty CONNECTED = MultiBlockProperties.CONNECTED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RealityBreakthroughReactorController() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(CONNECTED);
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

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            RealityBreakthroughRectorControllerTileEntity tileEntity = (RealityBreakthroughRectorControllerTileEntity) world.getBlockEntity(blockPos);
            if (tileEntity != null)
                tileEntity.deconstructMultiBlock();
        }
        super.onRemove(state, world, blockPos, newState, isMoving);
    }
}
