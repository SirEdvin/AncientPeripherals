package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;

import java.util.Collections;
import java.util.List;

public class RBTExtractorPeripheral extends GenericMachineryPeripheral<RBTExtractorControllerTileEntity> {

    public RBTExtractorPeripheral(String type, RBTExtractorControllerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(FreeMachineryOperation.EXTRACT);
    }
}
