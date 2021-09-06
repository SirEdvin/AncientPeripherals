package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkAmplifier;

import java.util.HashMap;
import java.util.Map;

public class EnderwireDimensionBreakerTileEntity extends BaseEnderwireTileEntity<EnderwireDimensionBreakerTileEntity, BasePeripheral<?>> {

    public EnderwireDimensionBreakerTileEntity() {
        super(TileEntityTypes.ENDERWIRE_DIMENSION_BREAKER.get());
    }

    @Override
    public EnderwireDimensionBreakerTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.DIMENSION_BREAKER;
    }

    @Override
    public @NotNull NetworkAmplifier getAmplifier() {
        return NetworkAmplifier.MAKE_INTERDIMENSIONAL;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }
}
