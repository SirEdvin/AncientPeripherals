package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;

public class FlexibleStatue extends BaseBlock {
    public static final BooleanProperty CONFIGURED = BooleanProperty.create("configured");

    public FlexibleStatue() {
        super(Properties.of(Material.DECORATION).dynamicShape());
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
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FlexibleStatueTileEntity();
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

    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof FlexibleStatueTileEntity) {
            if (!world.isClientSide) {
                ItemStack itemstack = new ItemStack(Blocks.FLEXIBLE_STATUE.get().asItem());
                CompoundNBT internalData = tileentity.save(new CompoundNBT());
                if (!internalData.isEmpty()) {
                    itemstack.addTagElement("BlockEntityTag", internalData);
                }

//                if (shulkerboxtileentity.hasCustomName()) {
//                    itemstack.setHoverName(shulkerboxtileentity.getCustomName());
//                }

                ItemEntity itementity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                world.addFreshEntity(itementity);
            }
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        super.setPlacedBy(p_180633_1_, p_180633_2_, p_180633_3_, p_180633_4_, p_180633_5_);
        System.out.println(p_180633_1_.toString());
    }
}
