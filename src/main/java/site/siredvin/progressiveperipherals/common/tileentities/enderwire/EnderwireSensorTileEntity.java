package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.EnderwireElementType;

public class EnderwireSensorTileEntity extends BaseEnderwireTileEntity<EnderwireSensorTileEntity> {
    public EnderwireSensorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_SENSOR.get());
    }

    @Override
    public EnderwireSensorTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.SENSOR;
    }
}
