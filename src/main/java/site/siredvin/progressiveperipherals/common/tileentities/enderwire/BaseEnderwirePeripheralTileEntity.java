package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTPeripheralTileEntity;
import site.siredvin.progressiveperipherals.extra.network.IEnderwireElement;

import java.util.UUID;

public abstract class BaseEnderwirePeripheralTileEntity<T extends TileEntity & IEnderwireElement<T>, V  extends BasePeripheral> extends MutableNBTPeripheralTileEntity<V> implements IEnderwireElement<T> {
    private static final String ATTACHED_NETWORK_TAG = "attachedNetwork";
    private static final String ELEMENT_UUID_TAG = "elementUUID";
    private static final String OWNER_UUID_TAG = "ownerUUID";

    protected @Nullable String attachedNetwork;
    protected UUID elementUUID;

    public BaseEnderwirePeripheralTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.elementUUID = UUID.randomUUID();
    }

    @Override
    public @Nullable String getAttachedNetwork() {
        return attachedNetwork;
    }

    @Override
    public void setAttachedNetwork(@Nullable String attachedNetwork) {
        this.attachedNetwork = attachedNetwork;
        pushState();
    }

    @Override
    public UUID getElementUUID() {
        return elementUUID;
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data.putUUID(ELEMENT_UUID_TAG, elementUUID);
        if (attachedNetwork != null)
            data.putString(ATTACHED_NETWORK_TAG, attachedNetwork);
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        elementUUID = data.getUUID(ELEMENT_UUID_TAG);
        if (data.contains(ATTACHED_NETWORK_TAG))
            attachedNetwork = data.getString(ATTACHED_NETWORK_TAG);
    }
}
