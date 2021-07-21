package site.siredvin.progressiveperipherals.common.blocks.rbtreactor;

import net.minecraft.block.AbstractBlock;
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
import site.siredvin.progressiveperipherals.common.blocks.base.BaseBlock;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockProperties;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockUtils;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class RealityBreakthroughReactorBlock extends BaseBlock {

    public static final BooleanProperty CONNECTED = MultiBlockProperties.CONNECTED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public RealityBreakthroughReactorBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONNECTED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
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

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            MultiBlockUtils.handlePartDestroy(
                    world, blockPos, RealityBreakthroughRectorControllerTileEntity.SIZE, blockState -> state.is(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get())
            );
        }
        super.onRemove(state, world, blockPos, newState, isMoving);
    }
}
