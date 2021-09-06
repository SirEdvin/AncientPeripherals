package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkAmplifier;

import java.util.HashMap;
import java.util.Map;

public class EnderwireNetworkAmplifierTileEntity extends BaseEnderwireTileEntity<EnderwireNetworkAmplifierTileEntity, BasePeripheral<?>> {
    
    public EnderwireNetworkAmplifierTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_AMPLIFIER.get());
    }

    @Override
    public EnderwireNetworkAmplifierTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.NETWORK_AMPLIFIER;
    }

    @Override
    public @NotNull NetworkAmplifier getAmplifier() {
        return NetworkAmplifier.EXTEND_RANGE;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }
}
