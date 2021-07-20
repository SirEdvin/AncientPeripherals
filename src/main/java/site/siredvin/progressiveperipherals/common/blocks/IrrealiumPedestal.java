package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.blocks.base.BasePedestal;
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

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (!itemInHand.isEmpty()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof IrrealiumPedestalTileEntity) {
                IrrealiumPedestalTileEntity pedestalTileEntity = (IrrealiumPedestalTileEntity) tileEntity;
                if (!pedestalTileEntity.hasStoredStack()) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    pedestalTileEntity.setStoredStack(itemInHand);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        ItemStack itemInHand = player.getItemInHand(Hand.MAIN_HAND);
        if (itemInHand.isEmpty()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof IrrealiumPedestalTileEntity) {
                IrrealiumPedestalTileEntity pedestalTileEntity = (IrrealiumPedestalTileEntity) tileEntity;
                if (pedestalTileEntity.hasStoredStack()) {
                    ItemStack storedStack = pedestalTileEntity.getStoredStack();
                    pedestalTileEntity.setStoredStack(ItemStack.EMPTY);
                    player.setItemInHand(Hand.MAIN_HAND, storedStack);
                }
            }
        }
        super.attack(state, world, pos, player);
    }
}
