package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class RealityBreakthroughReactorControllerPeripheral extends BasePeripheral {
    private final RealityBreakthroughRectorControllerTileEntity tileEntity;
    public RealityBreakthroughReactorControllerPeripheral(String type, RealityBreakthroughRectorControllerTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
