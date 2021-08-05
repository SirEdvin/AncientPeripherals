package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireLightEmitterTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

public class EnderwireLightEmitterBlock extends BaseEnderwireBlock<EnderwireLightEmitterTileEntity> {
    private static final VoxelShape SHAPE = VoxelShapes.or(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(5, 1, 5, 11, 7, 11)
    );
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    public EnderwireLightEmitterBlock() {
        super(BlockUtils.decoration());
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
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireLightEmitterTileEntity();
    }
}
