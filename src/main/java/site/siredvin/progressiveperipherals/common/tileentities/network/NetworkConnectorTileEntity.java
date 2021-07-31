package site.siredvin.progressiveperipherals.common.tileentities.network;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.enderwire.IEnderwireElement;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;

public class NetworkConnectorTileEntity extends MutableNBTTileEntity implements IEnderwireElement {
    private static final String ATTACHED_NETWORK_TAG = "attachedNetwork";

    private @Nullable String attachedNetwork;

    public NetworkConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_CONNECTOR.get());
    }

    @Override
    public @Nullable String getAttachedNetwork() {
        return attachedNetwork;
    }

    @Override
    public void setAttachedNetwork(@NotNull String attachedNetwork) {
        this.attachedNetwork = attachedNetwork;
        pushState();
    }

    @Override
    public void clearAttachedNetwork() {
        this.attachedNetwork = null;
        pushState();
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (attachedNetwork != null)
            data.putString(ATTACHED_NETWORK_TAG, attachedNetwork);
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains(ATTACHED_NETWORK_TAG))
            attachedNetwork = data.getString(ATTACHED_NETWORK_TAG);
    }
}
