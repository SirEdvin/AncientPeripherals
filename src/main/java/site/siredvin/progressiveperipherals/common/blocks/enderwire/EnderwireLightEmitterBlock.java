package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireLightEmitterTileEntity;
import site.siredvin.progressiveperipherals.utils.ShapeUtils;

public class EnderwireLightEmitterBlock extends BaseEnderwireHorizontalBlock<EnderwireLightEmitterTileEntity> {

    private static final VoxelShape FLOOR_SHAPE = VoxelShapes.or(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(5, 1, 5, 11, 7, 11)
    );

    private static final VoxelShape CELLING_SHAPE = VoxelShapes.or(
            Block.box(4, 15, 4, 12, 16, 12),
            Block.box(5, 9, 5, 11, 15, 11)
    );

    private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(
            Block.box(4, 4, 0, 12, 12, 1),
            Block.box(5, 5, 1, 11, 11, 7)
    );

    private static final VoxelShape SOUTH_SHAPE = ShapeUtils.rotateShape(Direction.NORTH, Direction.SOUTH, NORTH_SHAPE);
    private static final VoxelShape EAST_SHAPE = ShapeUtils.rotateShape(Direction.NORTH, Direction.EAST, NORTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = ShapeUtils.rotateShape(Direction.NORTH, Direction.WEST, NORTH_SHAPE);

    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    public EnderwireLightEmitterBlock() {
        super();
    }

    public BlockState generateDefaultState(BlockState parentDefaultState) {
        return super.generateDefaultState(parentDefaultState).setValue(ENABLED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ENABLED);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.getValue(ENABLED))
            return 15;
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.getValue(ENABLED))
            return 1.0F;
        return super.getShadeBrightness(state, world, pos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACE)) {
            case FLOOR:
                return FLOOR_SHAPE;
            case CEILING:
                return CELLING_SHAPE;
        }
        switch (state.getValue(FACING)) {
            case NORTH:
                return SOUTH_SHAPE;
            case SOUTH:
                return NORTH_SHAPE;
            case EAST:
                return WEST_SHAPE;
            case WEST:
                return EAST_SHAPE;
        }
        return FLOOR_SHAPE;
    }

    @Override
    public @NotNull EnderwireLightEmitterTileEntity newTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireLightEmitterTileEntity();
    }
}
