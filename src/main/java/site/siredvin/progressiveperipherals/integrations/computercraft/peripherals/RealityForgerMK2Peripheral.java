package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.RealityForgerTileEntity;

public class RealityForgerMK2Peripheral extends RealityForgerPeripheral {
    public static final String TYPE = "realityForgerMK2";

    public RealityForgerMK2Peripheral(RealityForgerTileEntity tileEntity) {
        super(TYPE, tileEntity);
    }

    @Override
    public int _getInteractionRadius() {
        return ProgressivePeripheralsConfig.realityForgerMK2Radius;
    }
}
