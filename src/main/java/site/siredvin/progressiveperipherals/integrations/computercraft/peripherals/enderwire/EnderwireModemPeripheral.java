package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import com.google.common.collect.MapMaker;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.PeripheralAPI;
import dan200.computercraft.core.asm.PeripheralMethod;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwirePeripheralConnectorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EnderwireModemPeripheral extends BasePeripheral {

    private static class PeripheralRecord {
        private final @NotNull IPeripheral peripheral;
        private final @NotNull String ownerName;
        private final @NotNull String name;
        private final @NotNull Map<String, PeripheralMethod> methodMap;

        public PeripheralRecord(@NotNull IPeripheral peripheral, @NotNull String ownerName, @NotNull String name) {
            this.peripheral = peripheral;
            this.ownerName = ownerName;
            this.name = name;
            this.methodMap = PeripheralAPI.getMethods(this.peripheral);
        }

        public @NotNull IPeripheral getPeripheral() {
            return peripheral;
        }

        public @NotNull String getOwnerName() {
            return ownerName;
        }

        public @NotNull String getName() {
            return name;
        }

        public @NotNull Collection<String> getMethodNames() {
            return methodMap.keySet();
        }

        public MethodResult callMethod(IComputerAccess access, ILuaContext context, String methodName, IArguments arguments) throws LuaException {
            PeripheralMethod method = this.methodMap.get(methodName);
            if (method == null) {
                throw new LuaException("No such method " + methodName);
            }
            return method.apply(this.peripheral, context, access, arguments);
        }
    }

    private final ConcurrentMap<String, PeripheralRecord> peripheralsRecord = new ConcurrentHashMap<>();
    private final Set<IPeripheral> sharedPeripherals = Collections.newSetFromMap(new MapMaker().concurrencyLevel(4).weakKeys().makeMap());


    public EnderwireModemPeripheral(@NotNull EnderwirePeripheralConnectorTileEntity tileEntity) {
        super("modem", tileEntity);
    }

    protected String selectName(@NotNull String networkName, @NotNull IPeripheral peripheral) {
        String elementType = networkName + "->" + peripheral.getType();
        int maxIndex = peripheralsRecord.keySet().stream().filter(key -> key.startsWith(elementType)).map(key -> {
            String[] splitName = key.split("_");
            return Integer.valueOf(splitName[splitName.length - 1]);
        }).max(Integer::compareTo).orElse(0);
        return elementType + "_" + (maxIndex + 1);
    }

    @Override
    public boolean isEnabled() {
        return EnderwireElementType.PERIPHERAL_CONNECTOR.isEnabled();
    }

    public void addSharedPeripheral(@NotNull String networkName, @NotNull EnderwireNetworkElement element, @NotNull IPeripheral peripheral) {
        if (sharedPeripherals.contains(peripheral))
            return;
        ProgressivePeripherals.LOGGER.warn(String.format("Adding peripheral from element %s", element.getName()));
        String peripheralName = selectName(networkName, peripheral);
        connectedComputers.forEach(computer -> {
            peripheral.attach(computer);
            computer.queueEvent("peripheral", peripheralName);
        });
        sharedPeripherals.add(peripheral);
        peripheralsRecord.put(peripheralName, new PeripheralRecord(peripheral, element.getName(), peripheralName));
    }

    public void removeSharedPeripheral(@NotNull EnderwireNetworkElement element) {
        ProgressivePeripherals.LOGGER.warn(String.format("Removing peripheral from element %s", element.getName()));
        Optional<PeripheralRecord> optRecord = peripheralsRecord.values().stream().filter(x -> x.getOwnerName().equals(element.getName())).findAny();
        optRecord.ifPresent(record -> {
            connectedComputers.forEach(computer -> {
                record.getPeripheral().detach(computer);
                computer.queueEvent("peripheral_detach", record.getName());
            });
            sharedPeripherals.remove(record.getPeripheral());
            peripheralsRecord.remove(record.getName());
        });
    }

    public void clearSharedPeripherals() {
        List<String> allKeys = new ArrayList<>(peripheralsRecord.keySet());
        allKeys.forEach(peripheralsRecord::remove);
    }

    @LuaFunction
    public final Collection<String> getNamesRemote() {
        return peripheralsRecord.keySet();
    }

    @LuaFunction
    public final boolean isPresentRemote(String name) {
        return peripheralsRecord.containsKey(name);
    }

    @LuaFunction
    public final boolean isWireless() {
        return false;
    }

    @LuaFunction
    public final Object[] getTypeRemote(IComputerAccess computer, String name) {
        PeripheralRecord record = peripheralsRecord.get(name);
        if (record == null)
            return null;
        return new Object[]{record.getPeripheral().getType()};
    }

    @LuaFunction
    public final Object[] getMethodsRemote(IComputerAccess computer, String name) {
        PeripheralRecord record = peripheralsRecord.get(name);
        if (record == null)
            return null;
        return new Object[]{record.getMethodNames()};
    }

    @LuaFunction
    public final MethodResult callRemote(IComputerAccess computer, ILuaContext context, IArguments arguments) throws LuaException {
        String remoteName = arguments.getString(0);
        String methodName = arguments.getString(1);
        PeripheralRecord record = peripheralsRecord.get(remoteName);
        if (record == null) {
            throw new LuaException("No peripheral: " + remoteName);
        }
        return record.callMethod(computer, context, methodName, arguments.drop(2));
    }
}
