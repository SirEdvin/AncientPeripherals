package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;

import java.util.Collections;
import java.util.List;

public class RBTReactorControllerPeripheral extends GenericMachineryPeripheral<RBTRectorControllerTileEntity> {

    public RBTReactorControllerPeripheral(String type, RBTRectorControllerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.emptyList();
    }
}