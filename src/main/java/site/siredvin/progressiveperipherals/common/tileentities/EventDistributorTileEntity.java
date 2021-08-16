package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.EventDistributorPeripheral;

public class EventDistributorTileEntity extends PeripheralTileEntity<EventDistributorPeripheral> {
    public EventDistributorTileEntity() {
        super(TileEntityTypes.EVENT_DISTRIBUTOR.get());
    }

    @Override
    protected @NotNull EventDistributorPeripheral createPeripheral() {
        return new EventDistributorPeripheral(this);
    }
}
