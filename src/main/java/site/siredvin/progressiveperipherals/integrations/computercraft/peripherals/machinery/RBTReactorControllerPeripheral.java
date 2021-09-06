package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;

public class RBTReactorControllerPeripheral extends GenericMachineryPeripheral<RBTRectorControllerTileEntity> {

    public static final String TYPE = "realityBreakthroughReactorController";

    public RBTReactorControllerPeripheral(RBTRectorControllerTileEntity tileEntity) {
        super(TYPE, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
