package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.network;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import net.minecraft.tileentity.TileEntity;

public class EnderwireNetworkConnectorPeripheral extends BasePeripheral {
    public <T extends TileEntity & IPeripheralTileEntity> EnderwireNetworkConnectorPeripheral(String type, T tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
