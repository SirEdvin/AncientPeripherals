package site.siredvin.progressiveperipherals.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.stream.Stream;

public abstract class BasePedestal<T extends TileEntity> extends TileEntityBlock<T> {
    private static final VoxelShape PEDESTAL = Stream.of(
            Block.box(3, 0, 3, 13, 1, 13),
            Block.box(6, 1, 6, 10, 9, 10),
            Block.box(4, 9, 4, 12, 10, 12),
            Block.box(5, 10, 5, 11, 11, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public BasePedestal(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return PEDESTAL;
    }
}
