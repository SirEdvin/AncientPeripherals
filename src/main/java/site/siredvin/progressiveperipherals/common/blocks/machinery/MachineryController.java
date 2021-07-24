package site.siredvin.progressiveperipherals.common.blocks.machinery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.common.blocks.GenericTileEntityBlock;
import site.siredvin.progressiveperipherals.common.machinery.MachineryBlockProperties;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;

public class MachineryController<T extends TileEntity & IMachineryController<T>> extends GenericTileEntityBlock<T> {
    public static final BooleanProperty CONNECTED = MachineryBlockProperties.CONNECTED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MachineryController(RegistryObject<TileEntityType<T>> tileEntityType) {
        super(tileEntityType);
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
            RBTRectorControllerTileEntity tileEntity = (RBTRectorControllerTileEntity) world.getBlockEntity(blockPos);
            if (tileEntity != null)
                tileEntity.deconstructMultiBlock();
        }
        super.onRemove(state, world, blockPos, newState, isMoving);
    }
}
