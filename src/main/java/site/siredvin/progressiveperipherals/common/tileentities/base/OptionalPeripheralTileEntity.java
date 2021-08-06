package site.siredvin.progressiveperipherals.common.tileentities.base;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.Capabilities;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IBasePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

import java.util.Collections;
import java.util.List;

public class OptionalPeripheralTileEntity<T extends IBasePeripheral> extends TileEntity implements IPeripheralTileEntity {

    private static final String PERIPHERAL_DATA_TAG = "peripheralData";

    // Peripheral logic
    protected @Nullable CompoundNBT peripheralData = null;
    @Nullable protected T peripheral = null;
    private LazyOptional<IPeripheral> peripheralCap;

    public OptionalPeripheralTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.peripheralData = new CompoundNBT();
    }

    @NotNull
    public <T1> LazyOptional<T1> getCapability(@NotNull Capability<T1> cap, @Nullable Direction direction) {
        if (cap == Capabilities.CAPABILITY_PERIPHERAL && hasPeripheral()) {
            if (this.peripheral == null) {
                this.peripheral = this.createPeripheral();
            }

            if (this.peripheral.isEnabled()) {
                if (this.peripheralCap == null) {
                    this.peripheralCap = LazyOptional.of(() -> {
                        return this.peripheral;
                    });
                } else if (!this.peripheralCap.isPresent()) {
                    this.peripheral = this.createPeripheral();
                    this.peripheralCap = LazyOptional.of(() -> {
                        return this.peripheral;
                    });
                }

                return this.peripheralCap.cast();
            }

            ProgressivePeripherals.LOGGER.info(this.peripheral.getType() + " is disabled, you can enable it in the Configuration.");
        }

        return super.getCapability(cap, direction);
    }

    protected void invalidateCaps() {
        super.invalidateCaps();
        if (this.peripheralCap != null) {
            this.peripheralCap.invalidate();
        }
    }

    @NotNull
    protected T createPeripheral() {
        throw new IllegalArgumentException("You should override this method, if you enable peripheral support!");
    }

    protected boolean hasPeripheral() {
        return false;
    }

    public List<IComputerAccess> getConnectedComputers() {
        return this.peripheral == null ? Collections.emptyList() : this.peripheral.getConnectedComputers();
    }

    @Override
    public CompoundNBT save(@NotNull CompoundNBT compound) {
        super.save(compound);
        if (this.peripheralData != null && !this.peripheralData.isEmpty()) {
            compound.put(PERIPHERAL_DATA_TAG, this.peripheralData);
        }

        return compound;
    }

    @Override
    public void load(@NotNull BlockState state, @NotNull CompoundNBT compound) {
        if (compound.contains(PERIPHERAL_DATA_TAG))
            this.peripheralData = compound.getCompound(PERIPHERAL_DATA_TAG);
        super.load(state, compound);
    }

    @Override
    public CompoundNBT getApSettings() {
        if (this.peripheralData == null)
            this.peripheralData = new CompoundNBT();
        return this.peripheralData;
    }
}
