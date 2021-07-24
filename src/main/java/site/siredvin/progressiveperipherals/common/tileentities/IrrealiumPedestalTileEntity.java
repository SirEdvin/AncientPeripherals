package site.siredvin.progressiveperipherals.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityStackContainer;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;

public class IrrealiumPedestalTileEntity extends MutableNBTTileEntity implements ITileEntityDataProvider, ITileEntityStackContainer {
    private static final String ITEM_STACK_TAG = "itemStackTag";
    private @NotNull ItemStack storedStack = ItemStack.EMPTY;

    public IrrealiumPedestalTileEntity() {
        super(TileEntityTypes.IRREALIUM_PEDESTAL.get());
    }

    public void setStoredStack(@NotNull ItemStack storedStack) {
        this.storedStack = storedStack;
        pushState();
    }

    public @NotNull ItemStack getStoredStack() {
        return storedStack;
    }

    @Override
    public boolean hasStoredStack() {
        return !storedStack.isEmpty();
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (!storedStack.isEmpty())
            data.put(ITEM_STACK_TAG, storedStack.serializeNBT());
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains(ITEM_STACK_TAG)) {
            storedStack = ItemStack.of(data.getCompound(ITEM_STACK_TAG));
            if (!skipUpdate)
                pushState();
        }
    }
}
