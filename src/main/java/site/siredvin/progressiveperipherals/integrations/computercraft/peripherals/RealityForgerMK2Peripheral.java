package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.RealityForgerTileEntity;

public class RealityForgerMK2Peripheral extends RealityForgerPeripheral {
    public RealityForgerMK2Peripheral(String type, RealityForgerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public int _getInteractionRadius() {
        return ProgressivePeripheralsConfig.realityForgerMK2Radius;
    }
}
