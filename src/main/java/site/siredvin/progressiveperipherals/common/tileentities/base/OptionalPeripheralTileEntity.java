package site.siredvin.progressiveperipherals.common.tileentities.base;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.Capabilities;
import de.srendi.advancedperipherals.lib.peripherals.IBasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.IPeripheralTileEntity;
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
import java.util.Objects;

public class OptionalPeripheralTileEntity<T extends IBasePeripheral<?>> extends TileEntity implements IPeripheralTileEntity {

    private static final String PERIPHERAL_DATA_TAG = "peripheralData";

    // Peripheral logic
    protected @NotNull CompoundNBT peripheralData;
    @Nullable protected T peripheral = null;
    private LazyOptional<IPeripheral> peripheralCap;

    public OptionalPeripheralTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.peripheralData = new CompoundNBT();
    }

    public void ensurePeripheralCreated() {
        if (this.peripheral == null) {
            this.peripheral = this.createPeripheral();
        }
    }

    @NotNull
    public <T1> LazyOptional<T1> getCapability(@NotNull Capability<T1> cap, @Nullable Direction direction) {
        if (cap == Capabilities.CAPABILITY_PERIPHERAL && hasPeripheral()) {
            ensurePeripheralCreated();
            Objects.requireNonNull(peripheral);

            if (this.peripheral.isEnabled()) {
                if (this.peripheralCap == null) {
                    this.peripheralCap = LazyOptional.of(() -> this.peripheral);
                } else if (!this.peripheralCap.isPresent()) {
                    this.peripheral = this.createPeripheral();
                    this.peripheralCap = LazyOptional.of(() -> this.peripheral);
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
    public @NotNull CompoundNBT save(@NotNull CompoundNBT compound) {
        super.save(compound);
        if (!this.peripheralData.isEmpty()) {
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
    public CompoundNBT getPeripheralSettings() {
        return peripheralData;
    }

    @Override
    public void markSettingsChanged() {
        setChanged();
    }
}
