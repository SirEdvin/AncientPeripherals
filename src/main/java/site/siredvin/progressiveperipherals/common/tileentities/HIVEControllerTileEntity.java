package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PoweredPeripheralTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.HIVEControllerPeripheral;

import java.util.UUID;

public class HIVEControllerTileEntity extends PoweredPeripheralTileEntity<HIVEControllerPeripheral> {

    public static final String CONTROLLED_ID_TAG = "hiveControllerID";
    protected UUID controllerID;

    public HIVEControllerTileEntity() {
        super(TileEntityTypes.HIVE_CONTROLLER.get());
    }

    @NotNull
    @Override
    protected HIVEControllerPeripheral createPeripheral() {
        return new HIVEControllerPeripheral("HIVEController", this);
    }

    @Override
    protected int getMaxEnergyStored() {
        return 100_000_000;
    }

    public UUID getControllerID() {
        if (controllerID == null)
            controllerID = UUID.randomUUID();
        return controllerID;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putString(CONTROLLED_ID_TAG, controllerID.toString());
        return super.save(compound);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.controllerID = UUID.fromString(compound.getString(CONTROLLED_ID_TAG));
    }
}
