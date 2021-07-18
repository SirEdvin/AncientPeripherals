package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.blocks.ITileEntityDataProvider;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public abstract class NBTBlock<T extends TileEntity & ITileEntityDataProvider> extends BaseBlock {
    public static final String INTERNAL_DATA_TAG = "internalData";
    public static final String BLOCK_STATE_TAG = "blockState";

    public NBTBlock() {
        this(Properties.of(Material.METAL).strength(1, 5).harvestLevel(0).sound(SoundType.METAL).noOcclusion().harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops());
    }

    public NBTBlock(Properties properties) {
        super(properties.strength(1, 5).harvestLevel(0).sound(SoundType.METAL).noOcclusion().harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public abstract @Nonnull TileEntity createTileEntity(BlockState state, IBlockReader world);

    public abstract @Nonnull ItemStack createItemStack();

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

                if (!savableProperties.isEmpty()) {
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
                CompoundNBT data = stack.getOrCreateTag();
                if (data.contains(BLOCK_STATE_TAG)) {
                    BlockState savedState = NBTUtil.readBlockState(data.getCompound(BLOCK_STATE_TAG));
                    for (Property property: savableProperties()) {
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
