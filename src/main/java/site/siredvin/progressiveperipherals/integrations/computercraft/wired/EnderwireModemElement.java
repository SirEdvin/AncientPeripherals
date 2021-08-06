package site.siredvin.progressiveperipherals.integrations.computercraft.wired;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.modem.wired.WiredModemElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EnderwireModemElement extends WiredModemElement {

    private final IEnderwireModemElementTileEntity<?> owner;

    public EnderwireModemElement(IEnderwireModemElementTileEntity<?> owner) {
        super();
        this.owner = owner;
    }

    @Nonnull
    @Override
    public World getWorld() {
        return owner.getWorld();
    }

    @Nonnull
    @Override
    public Vector3d getPosition() {
        BlockPos pos = owner.getPosition();
        return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    protected void attachPeripheral(String name, IPeripheral peripheral) {
        EnderwireModemPeripheral internalPeripheral = owner.getWiredPeripheral();
        if (internalPeripheral != null)
            internalPeripheral.attachPeripheral(name, peripheral);
    }

    @Override
    protected void detachPeripheral(String name) {
        EnderwireModemPeripheral internalPeripheral = owner.getWiredPeripheral();
        if (internalPeripheral != null)
            internalPeripheral.detachPeripheral(name);
    }
}
