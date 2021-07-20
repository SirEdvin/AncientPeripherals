package site.siredvin.progressiveperipherals.common.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;

public abstract class TileEntityBlock<T extends TileEntity> extends BaseBlock {
    public TileEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public abstract @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world);
}
