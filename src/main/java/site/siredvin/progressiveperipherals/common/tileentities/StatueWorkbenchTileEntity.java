package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.StatueWorkbenchPeripheral;

public class StatueWorkbenchTileEntity extends PeripheralTileEntity<StatueWorkbenchPeripheral> {
    public StatueWorkbenchTileEntity() {
        super(TileEntityTypes.STATUE_WORKBENCH.get());
    }

    @Override
    protected @NotNull StatueWorkbenchPeripheral createPeripheral() {
        return new StatueWorkbenchPeripheral(this);
    }
}
