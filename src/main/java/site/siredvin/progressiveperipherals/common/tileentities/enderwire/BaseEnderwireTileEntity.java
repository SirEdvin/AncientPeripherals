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

public abstract class BaseEnderwireTileEntity<T extends TileEntity & IEnderwireElement<T>, V  extends IBasePeripheral> extends MutableNBTTileEntity<V> implements IEnderwireElement<T>, IBlockObservingTileEntity {
    private static final String ATTACHED_NETWORK_TAG = "attachedNetwork";
    private static final String ELEMENT_NAME_TAG = "elementName";

    protected @Nullable String attachedNetwork;
    protected @Nullable String name;

    private boolean requireNetworkCheck = true;

    public BaseEnderwireTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
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
    public @Nullable String getElementName() {
        return name;
    }

    @Override
    public void setElementName(@Nullable String name) {
        this.name = name;
    }


    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (name != null)
            data.putString(ELEMENT_NAME_TAG, name);
        if (attachedNetwork != null)
            data.putString(ATTACHED_NETWORK_TAG, attachedNetwork);
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data) {
        if (data.contains(ELEMENT_NAME_TAG))
            name = data.getString(ELEMENT_NAME_TAG);
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
                    setElementName(null);
                }
                if (!getElementType().isEnabled()) {
                    setAttachedNetwork(null);
                    setElementName(null);
                }
            }
            requireNetworkCheck = false;
        }
    }
}
