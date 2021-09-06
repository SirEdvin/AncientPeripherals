package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.computer.ComputerSystem;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TileEntityPeripheralOwner;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.EventDistributorTileEntity;

import java.util.Optional;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUEUE_EVENT;

public class EventDistributorPeripheral extends BasePeripheral<TileEntityPeripheralOwner<EventDistributorTileEntity>> {
    public static final String TYPE = "eventDistributor";

    public EventDistributorPeripheral(EventDistributorTileEntity tileEntity) {
        super(TYPE, new TileEntityPeripheralOwner<>(tileEntity));
        owner.attachOperation(QUEUE_EVENT);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableEventDistributor;
    }

    private void sendEvent(@NotNull IArguments arguments, String eventName, IComputerAccess access) {
        int argumentCount = arguments.count();
        Object[] eventData = new Object[argumentCount - 2];
        for (int i = 2; i < argumentCount; i++) {
            eventData[i - 2] = arguments.get(i);
        }
        access.queueEvent(eventName, eventData);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult broadcastEvent(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUEUE_EVENT, null, null, ignored -> {
            String eventName = arguments.getString(0);
            int argumentCount = arguments.count();
            Object[] eventData = new Object[argumentCount - 1];
            for (int i = 1; i < argumentCount; i++) {
                eventData[i - 1] = arguments.get(i);
            }
            getConnectedComputers().forEach(computer -> computer.queueEvent(eventName, eventData));
            return MethodResult.of(true);
        }, null);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult sendEventByID(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUEUE_EVENT, null, null, ignored -> {
            int computerID = arguments.getInt(0);
            String eventName = arguments.getString(1);
            Optional<IComputerAccess> targetAccess = getConnectedComputers().stream().filter(computer -> computer.getID() == computerID).findAny();
            if (!targetAccess.isPresent())
                return MethodResult.of(null, "Cannot find computer");
            sendEvent(arguments, eventName, targetAccess.get());
            return MethodResult.of(true);
        }, null);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult sendEventByLabel(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUEUE_EVENT, null, null, ignored -> {
            String computerLabel = arguments.getString(0);
            String eventName = arguments.getString(1);
            Optional<IComputerAccess> targetAccess = getConnectedComputers().stream().filter(computer -> {
                if (computer instanceof ComputerSystem)
                    return computerLabel.equals(((ComputerSystem) computer).getLabel());
                return false;
            }).findAny();
            if (!targetAccess.isPresent())
                return MethodResult.of(null, "Cannot find computer");
            sendEvent(arguments, eventName, targetAccess.get());
            return MethodResult.of(true);
        }, null);
    }
}
