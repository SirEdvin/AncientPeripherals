package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.computercraft.peripherals.RealityForgerPeripheral;

public class RealityForgerTileEntity extends PeripheralTileEntity<RealityForgerPeripheral> {
    public RealityForgerTileEntity() {
        super(TileEntityTypes.REALITY_FORGER.get());
    }

    @Override
    protected @NotNull RealityForgerPeripheral createPeripheral() {
        return new RealityForgerPeripheral("realityForger", this);
    }
}
