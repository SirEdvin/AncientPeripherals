package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.RealityForgerMK2Peripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.RealityForgerPeripheral;

public class RealityForgerTileEntity extends PeripheralTileEntity<RealityForgerPeripheral> {
    public RealityForgerTileEntity() {
        super(TileEntityTypes.REALITY_FORGER.get());
    }

    @Override
    protected @NotNull RealityForgerPeripheral createPeripheral() {
        BlockState blockState = getBlockState();
        if (blockState.is(Blocks.REALITY_FORGER_MK2.get()))
            return new RealityForgerMK2Peripheral("realityForgerMK2", this);
        return new RealityForgerPeripheral("realityForger", this);
    }
}
