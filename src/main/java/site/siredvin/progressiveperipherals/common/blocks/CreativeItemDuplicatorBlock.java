package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.CreativeItemDuplicatorTileEntity;

public class CreativeItemDuplicatorBlock extends IrrealiumPedestal {
    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CreativeItemDuplicatorTileEntity();
    }
}
