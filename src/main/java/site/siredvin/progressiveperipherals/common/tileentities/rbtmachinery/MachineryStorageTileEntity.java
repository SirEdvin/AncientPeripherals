package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class MachineryStorageTileEntity extends LockableLootTileEntity {

    private static final String SIZE_TAG = "size";

    private NonNullList<ItemStack> items;

    public MachineryStorageTileEntity() {
        this(27);
    }

    public MachineryStorageTileEntity(int size) {
        super(TileEntityTypes.MACHINERY_STORAGE.get());
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public CompoundNBT saveInternalData(CompoundNBT data) {
        data.putInt(SIZE_TAG, items.size());
        ItemStackHelper.saveAllItems(data, items);
        return data;
    }

    public void loadInternalData(BlockState state, CompoundNBT data) {
        int size = data.getInt(SIZE_TAG);
        if (items.size() != size) {
            int inflictedSize = Math.min(items.size(), size);
            NonNullList<ItemStack> newItems = NonNullList.withSize(size, ItemStack.EMPTY);
            for (int i = 0; i < inflictedSize; i++) {
                newItems.set(i, items.get(i));
            }
            items = newItems;
        }
        ItemStackHelper.loadAllItems(data, items);
    }

    @Override
    public @NotNull CompoundNBT save(@NotNull CompoundNBT data) {
        return saveInternalData(super.save(data));
    }

    @Override
    public void load(@NotNull BlockState state, @NotNull CompoundNBT data) {
        super.load(state, data);
        loadInternalData(state, data);
    }

    @Override
    protected @NotNull ITextComponent getDefaultName() {
        return TranslationUtil.localization("machinery_storage");
    }

    @Override
    protected @NotNull Container createMenu(int p_213906_1_, @NotNull PlayerInventory p_213906_2_) {
        if (items.size() == 54)
            return ChestContainer.sixRows(p_213906_1_, p_213906_2_, this);
        return ChestContainer.threeRows(p_213906_1_, p_213906_2_, this);
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> nonNullList) {
        items = nonNullList;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }
}
