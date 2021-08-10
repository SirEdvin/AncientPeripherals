package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.base.IBasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.IBlockObservingTileEntity;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.BaseEnderwireBlock;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;

import java.util.UUID;

public abstract class BaseEnderwireTileEntity<T extends TileEntity & IEnderwireElement<T>, V  extends IBasePeripheral> extends MutableNBTTileEntity<V> implements IEnderwireElement<T>, IBlockObservingTileEntity {
    private static final String ATTACHED_NETWORK_TAG = "attachedNetwork";
    private static final String ELEMENT_UUID_TAG = "elementUUID";

    protected @Nullable String attachedNetwork;
    protected UUID elementUUID;

    private boolean requireNetworkCheck = true;

    public BaseEnderwireTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.elementUUID = UUID.randomUUID();
    }

    @Override
    public @Nullable String getAttachedNetwork() {
        return attachedNetwork;
    }

    public BlockState handleAttachedNetwork(@Nullable String attachedNetwork) {
        return getBlockState().setValue(BaseEnderwireBlock.CONNECTED, attachedNetwork != null);
    }

    public void onAttachedNetworkChange(String oldNetwork, String newNetwork) {

    }

    @Override
    public final void setAttachedNetwork(@Nullable String attachedNetwork) {
        String oldNetwork = this.attachedNetwork;
        this.attachedNetwork = attachedNetwork;
        pushInternalDataChangeToClient(handleAttachedNetwork(attachedNetwork));
        onAttachedNetworkChange(oldNetwork, this.attachedNetwork);
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
    public void loadInternalData(BlockState state, CompoundNBT data) {
        elementUUID = data.getUUID(ELEMENT_UUID_TAG);
        if (data.contains(ATTACHED_NETWORK_TAG)) {
            attachedNetwork = data.getString(ATTACHED_NETWORK_TAG);
        } else {
            attachedNetwork = null;
        }
        // queue network check
        requireNetworkCheck = true;
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void blockTick() {
        if (requireNetworkCheck) {
            if (level != null && !level.isClientSide && attachedNetwork != null) {
                GlobalNetworksData networksData = GlobalNetworksData.get((ServerWorld) level);
                if (!networksData.networkExists(attachedNetwork)) {
                    setAttachedNetwork(null);
                }
                if (!getElementType().isEnabled())
                    setAttachedNetwork(null);
            }
            requireNetworkCheck = false;
        }
    }
}
