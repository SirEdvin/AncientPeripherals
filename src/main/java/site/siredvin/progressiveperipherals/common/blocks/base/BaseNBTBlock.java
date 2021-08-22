package site.siredvin.progressiveperipherals.common.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;

import java.util.Collections;
import java.util.List;

public abstract class BaseNBTBlock<T extends TileEntity & ITileEntityDataProvider> extends TileEntityBlock<T> {
    public static final String INTERNAL_DATA_TAG = "internalData";
    public static final String BLOCK_STATE_TAG = "blockState";

    public BaseNBTBlock(Properties properties) {
        super(properties);
    }

    @Override
    public abstract @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world);

    public abstract @NotNull ItemStack createItemStack();

    public List<Property<?>> savableProperties() {
        return Collections.emptyList();
    }

    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof ITileEntityDataProvider) {
            if (!world.isClientSide && !player.isCreative()) {
                ItemStack itemstack = createItemStack();
                CompoundNBT internalData = ((ITileEntityDataProvider) tileentity).saveInternalData(new CompoundNBT());
                if (!internalData.isEmpty()) {
                    itemstack.addTagElement(INTERNAL_DATA_TAG, internalData);
                }
                List<Property<?>> savableProperties = savableProperties();

                if (!savableProperties.isEmpty() && !defaultBlockState().equals(state)) {
                    itemstack.addTagElement(BLOCK_STATE_TAG, NBTUtil.writeBlockState(state));
                }

                ItemEntity itementity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                world.addFreshEntity(itementity);
            }
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof ITileEntityDataProvider) {
            if (!world.isClientSide) {
                CompoundNBT data = stack.getTag();
                if (data != null) {
                    if (data.contains(BLOCK_STATE_TAG)) {
                        BlockState savedState = NBTUtil.readBlockState(data.getCompound(BLOCK_STATE_TAG));
                        for (Property property : savableProperties()) {
                            state = state.setValue(property, savedState.getValue(property));
                        }
                    }
                    if (data.contains(INTERNAL_DATA_TAG)) {
                        ((ITileEntityDataProvider) tileentity).loadInternalData(state, data.getCompound(INTERNAL_DATA_TAG), false);
                    }
                }
            }
        }
    }
}
