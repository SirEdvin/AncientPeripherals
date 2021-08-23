package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireModemTileEntity;

public class EnderwireModemPeripheral extends BaseEnderwireModemPeripheral {
    private final EnderwireModemTileEntity tileEntity;

    public EnderwireModemPeripheral(@NotNull EnderwireModemTileEntity tileEntity) {
        super(tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    protected String buildElementType(@NotNull String networkName, @NotNull IPeripheral peripheral) {
        return networkName + "->" + tileEntity.getElementName() + "->" + peripheral.getType();
    }
}
