package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.computer.ComputerSystem;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUEUE_EVENT;

public class EventDistributorPeripheral extends OperationPeripheral {
    public static final String TYPE = "eventDistributor";

    public EventDistributorPeripheral(PeripheralTileEntity<?> tileEntity) {
        super(TYPE, tileEntity);
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(QUEUE_EVENT);
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

    @SuppressWarnings("unused")
    @LuaFunction
    public final MethodResult broadcastEvent(@NotNull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResult = cooldownCheck(QUEUE_EVENT);
        if (checkResult.isPresent())
            return checkResult.get();
        String eventName = arguments.getString(0);
        int argumentCount = arguments.count();
        Object[] eventData = new Object[argumentCount - 1];
        for (int i = 1; i < argumentCount; i++) {
            eventData[i - 1] = arguments.get(i);
        }
        getConnectedComputers().forEach(computer -> computer.queueEvent(eventName, eventData));
        trackOperation(QUEUE_EVENT, null);
        return MethodResult.of(true);
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final MethodResult sendEventByID(@NotNull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResult = cooldownCheck(QUEUE_EVENT);
        if (checkResult.isPresent())
            return checkResult.get();
        int computerID = arguments.getInt(0);
        String eventName = arguments.getString(1);
        Optional<IComputerAccess> targetAccess = getConnectedComputers().stream().filter(computer -> computer.getID() == computerID).findAny();
        if (!targetAccess.isPresent())
            return MethodResult.of(null, "Cannot find computer");
        sendEvent(arguments, eventName, targetAccess.get());
        trackOperation(QUEUE_EVENT, null);
        return MethodResult.of(true);
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final MethodResult sendEventByLabel(@NotNull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResult = cooldownCheck(QUEUE_EVENT);
        if (checkResult.isPresent())
            return checkResult.get();
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
        trackOperation(QUEUE_EVENT, null);
        return MethodResult.of(true);
    }
}
