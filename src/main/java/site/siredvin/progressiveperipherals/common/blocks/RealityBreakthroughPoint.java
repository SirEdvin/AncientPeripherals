package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseBlock;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

public class RealityBreakthroughPoint extends BaseBlock {

    public RealityBreakthroughPoint() {
        super(BlockUtils.unbreakable());
    }

    @Override
    public @NotNull BlockRenderType getRenderShape(@NotNull BlockState blockState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RealityBreakthroughPointTileEntity();
    }
}
