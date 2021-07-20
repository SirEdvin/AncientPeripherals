package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.RealityBreakthroughReactorControllerPeripheral;

public class RealityBreakthroughRectorControllerTileEntity extends PeripheralTileEntity<RealityBreakthroughReactorControllerPeripheral> {
    public RealityBreakthroughRectorControllerTileEntity() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get());
    }

    @Override
    protected @NotNull RealityBreakthroughReactorControllerPeripheral createPeripheral() {
        return new RealityBreakthroughReactorControllerPeripheral("realityBreakthroughReactorController", this);
    }
}
