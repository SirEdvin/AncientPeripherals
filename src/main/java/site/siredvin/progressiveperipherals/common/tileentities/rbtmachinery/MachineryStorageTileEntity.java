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
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class MachineryStorageTileEntity extends LockableLootTileEntity implements ITileEntityDataProvider {

    private NonNullList<ItemStack> items;

    public MachineryStorageTileEntity() {
        this(27);
    }

    public MachineryStorageTileEntity(int size) {
        super(TileEntityTypes.MACHINERY_STORAGE.get());
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        ItemStackHelper.saveAllItems(data, items);
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        ItemStackHelper.loadAllItems(data, items);
    }

    @Override
    public CompoundNBT save(CompoundNBT data) {
        return saveInternalData(super.save(data));
    }

    @Override
    public void load(BlockState state, CompoundNBT data) {
        super.load(state, data);
        loadInternalData(state, data, true);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TranslationUtil.localization("machinery_storage");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return ChestContainer.threeRows(p_213906_1_, p_213906_2_, this);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        items = nonNullList;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }
}
