package site.siredvin.progressiveperipherals.integrations.computercraft.wired;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wired.WiredModemLocalPeripheral;
import dan200.computercraft.shared.peripheral.modem.wired.WiredModemPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IBasePeripheral;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EnderwireModemPeripheral extends WiredModemPeripheral implements IBasePeripheral {

    private final List<IComputerAccess> connectedComputers = new ArrayList<>();

    private final IEnderwireModemTileEntity<?> owner;

    public EnderwireModemPeripheral(IEnderwireModemTileEntity<?> owner) {
        super(new ModemState(() -> SingleTickScheduler.schedule(owner.getThis())), owner.getWiredElement());
        this.owner = owner;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void attach(@NotNull IComputerAccess computer) {
        super.attach(computer);
        connectedComputers.add(computer);
    }

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        super.detach(computer);
        connectedComputers.remove(computer);
    }

    @Override
    public List<IComputerAccess> getConnectedComputers() {
        return connectedComputers;
    }

    @Nonnull
    @Override
    protected WiredModemLocalPeripheral getLocalPeripheral() {
        return owner.getLocalPeripheral();
    }

    @Nonnull
    @Override
    public Vector3d getPosition() {
        return owner.getWiredPosition();
    }

    @Nonnull
    @Override
    public Object getTarget() {
        return owner;
    }
}
