package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.network.wired.IWiredElement;
import dan200.computercraft.api.network.wired.IWiredNode;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.modem.wired.WiredModemLocalPeripheral;
import dan200.computercraft.shared.util.CapabilityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.integrations.computercraft.wired.EnderwireModemElement;
import site.siredvin.progressiveperipherals.integrations.computercraft.wired.EnderwireModemPeripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.wired.IEnderwireModemTileEntity;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static dan200.computercraft.shared.Capabilities.CAPABILITY_PERIPHERAL;
import static dan200.computercraft.shared.Capabilities.CAPABILITY_WIRED_ELEMENT;

public abstract class BaseEnderwireWiredTileEntity<T extends TileEntity & IEnderwireElement<T>> extends BaseEnderwireTileEntity<T, EnderwireModemPeripheral> implements IEnderwireModemTileEntity<T> {

    protected final WiredModemLocalPeripheral localPeripheral = new WiredModemLocalPeripheral(this::refreshPeripheral);
    protected final EnderwireModemElement element = new EnderwireModemElement(this);
    private LazyOptional<IWiredElement> elementCap;
    protected final IWiredNode node = element.getNode();
    protected final EnderwireModemPeripheral modem = new EnderwireModemPeripheral(this);

    private LazyOptional<IPeripheral> modemCap;
    protected boolean destroyed = false;
    protected boolean initialized = false;

    public BaseEnderwireWiredTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    private void onRemove() {
        if (level == null || !level.isClientSide) {
            node.remove();
        }
    }

    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            modem.destroy();
            onRemove();
        }
    }

    @Override
    public void placed() {
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        onRemove();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        onRemove();
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        elementCap = CapabilityUtil.invalidate(elementCap);
        modemCap = CapabilityUtil.invalidate(modemCap);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void clearCache() {
        super.clearCache();
        SingleTickScheduler.now(this);
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data) {
        super.loadInternalData(state, data);
        localPeripheral.read(data, "");
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data = super.saveInternalData(data);
        localPeripheral.write(data, "");
        return data;
    }

    @Nonnull
    @Override
    public <T1> LazyOptional<T1> getCapability(@Nonnull Capability<T1> capability, @Nullable Direction side) {
        if (capability == CAPABILITY_WIRED_ELEMENT) {
            if (destroyed)
                return LazyOptional.empty();
            if (elementCap == null)
                elementCap = LazyOptional.of(() -> element);
            return elementCap.cast();
        }

        if (capability == CAPABILITY_PERIPHERAL) {
            if (modemCap == null) modemCap = LazyOptional.of(() -> modem);
            return modemCap.cast();
        }

        return super.getCapability(capability, side);
    }

    @Override
    public @NotNull EnderwireModemPeripheral getWiredPeripheral() {
        return modem;
    }

    @Override
    public @NotNull EnderwireModemElement getWiredElement() {
        return element;
    }

    @Override
    public @NotNull Vector3d getWiredPosition() {
        BlockPos pos = getBlockPos();
        return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public @NotNull WiredModemLocalPeripheral getLocalPeripheral() {
        return localPeripheral;
    }

    protected void refreshPeripheral() {
    }
}
