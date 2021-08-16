package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.utils.ShapeUtils;

public abstract class BaseEnderwirePlate<T extends TileEntity> extends BaseEnderwireHorizontalFaceBlock<T> {

    private static final VoxelShape DOWN_ATTACHED = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape UP_ATTACHED = Block.box(0, 14, 0, 16, 16, 16);
    private static final VoxelShape NORTH_ATTACHED = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.SOUTH, NORTH_ATTACHED);
    private static final VoxelShape EAST_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.EAST, NORTH_ATTACHED);
    private static final VoxelShape WEST_ATTACHED = ShapeUtils.rotateShape(Direction.NORTH, Direction.WEST, NORTH_ATTACHED);

    public BaseEnderwirePlate() {
        super();
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState p_185499_1_, @NotNull Rotation p_185499_2_) {
        // Rotation can cause strange problem, so avoiding it
        return p_185499_1_;
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState p_185471_1_, @NotNull Mirror p_185471_2_) {
        // Rotation can cause strange problem, so avoiding it
        return p_185471_1_;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos, @NotNull ISelectionContext context) {
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
}
