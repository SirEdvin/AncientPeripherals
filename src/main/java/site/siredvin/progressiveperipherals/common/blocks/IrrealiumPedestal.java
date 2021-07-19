package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.blocks.base.BasePedestal;
import site.siredvin.progressiveperipherals.common.tileentities.AbstractiumPedestalTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.IrrealiumPedestalTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

public class IrrealiumPedestal extends BasePedestal<IrrealiumPedestalTileEntity> {

    public IrrealiumPedestal() {
        super(BlockUtils.defaultProperties());
    }

    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IrrealiumPedestalTileEntity();
    }
}
