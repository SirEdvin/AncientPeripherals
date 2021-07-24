package site.siredvin.progressiveperipherals.common.blocks.machinery;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.MachineryStorageTileEntity;

public class MachineryStorage extends MachineryBlock {
    private final int size;

    public MachineryStorage(Properties properties, int size) {
        super(properties);
        this.size = size;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MachineryStorageTileEntity(size);
    }

    public void onRemove(BlockState oldState, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            MachineryStorageTileEntity tileEntity = (MachineryStorageTileEntity) world.getBlockEntity(blockPos);
            if (tileEntity != null) {
                InventoryHelper.dropContents(world, blockPos, tileEntity);
                world.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(oldState, world, blockPos, newState, isMoving);
        }
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return (MachineryStorageTileEntity) world.getBlockEntity(pos);
    }


    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            INamedContainerProvider containerProvider = this.getMenuProvider(blockState, world, blockPos);
            if (containerProvider != null) {
                playerEntity.openMenu(containerProvider);
            }

            return ActionResultType.CONSUME;
        }
    }
}
