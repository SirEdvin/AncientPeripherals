package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.blocks.base.TileEntityBlock;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import java.util.Objects;

public class GenericTileEntityBlock<T extends TileEntity> extends TileEntityBlock<T> {
    private final RegistryObject<TileEntityType<T>> tileEntityType;

    public GenericTileEntityBlock(RegistryObject<TileEntityType<T>> tileEntityType, Properties properties) {
        super(properties);
        this.tileEntityType = tileEntityType;
    }

    public GenericTileEntityBlock(RegistryObject<TileEntityType<T>> tileEntityType) {
        this(tileEntityType, BlockUtils.defaultProperties());
    }

    @NotNull
    @Override
    public T createTileEntity(BlockState state, IBlockReader world) {
        return Objects.requireNonNull(tileEntityType.get().create());
    }
}
