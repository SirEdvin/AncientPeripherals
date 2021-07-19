package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseNBTBlock;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

public class FlexibleStatue extends BaseNBTBlock<FlexibleStatueTileEntity> {
    public static final BooleanProperty CONFIGURED = BooleanProperty.create("configured");

    public FlexibleStatue() {
        super(BlockUtils.decoration().dynamicShape());
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONFIGURED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONFIGURED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FlexibleStatueTileEntity();
    }

    @Override
    public @NotNull ItemStack createItemStack() {
        return new ItemStack(Blocks.FLEXIBLE_STATUE.get().asItem());
    }

    protected VoxelShape getDefaultShape(IBlockReader world, BlockPos pos) {
        FlexibleStatueTileEntity tileEntity = (FlexibleStatueTileEntity) world.getBlockEntity(pos);
        if (tileEntity != null) {
            VoxelShape shape = tileEntity.getBlockShape();
            if (shape != null)
                return tileEntity.getBlockShape();
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = getDefaultShape(world, pos);
        if (shape != null)
            return shape;
        return super.getShape(state, world, pos, context);
    }
}
