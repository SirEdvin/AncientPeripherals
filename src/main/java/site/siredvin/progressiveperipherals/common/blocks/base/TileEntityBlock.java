package site.siredvin.progressiveperipherals.common.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

public abstract class TileEntityBlock<T extends TileEntity> extends BaseBlock {
    private RegistryObject<TileEntityType<T>> tileEntity;

    public TileEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public abstract @Nonnull TileEntity createTileEntity(BlockState state, IBlockReader world);
}
