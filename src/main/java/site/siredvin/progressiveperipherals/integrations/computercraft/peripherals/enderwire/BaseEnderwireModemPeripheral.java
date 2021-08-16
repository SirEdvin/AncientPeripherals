package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapMaker;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.filesystem.IWritableMount;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IWorkMonitor;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.core.apis.PeripheralAPI;
import dan200.computercraft.core.asm.PeripheralMethod;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BaseEnderwireModemPeripheral extends BasePeripheral {

    public static final String TYPE = "modem";

    private class PeripheralRecord {
        private final @NotNull IPeripheral peripheral;
        private final @NotNull String ownerName;
        private final @NotNull String name;
        private final @NotNull Map<String, PeripheralMethod> methodMap;
        private final ConcurrentMap<IComputerAccess, RemoteComputerWrapper> wrappers;

        public PeripheralRecord(@NotNull IPeripheral peripheral, @NotNull String ownerName, @NotNull String name) {
            this.peripheral = peripheral;
            this.ownerName = ownerName;
            this.name = name;
            this.methodMap = PeripheralAPI.getMethods(this.peripheral);
            wrappers = new ConcurrentHashMap<>();
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

        public void attach(IComputerAccess computer) {
            peripheral.attach(computer);
            computer.queueEvent("peripheral", name);
            wrappers.put(computer, new RemoteComputerWrapper(computer, this));
        }

        public void detach(IComputerAccess computer) {
            peripheral.detach(computer);
            computer.queueEvent("peripheral_detach", name);
            wrappers.remove(computer);
        }

        public MethodResult callMethod(IComputerAccess access, ILuaContext context, String methodName, IArguments arguments) throws LuaException {
            PeripheralMethod method = this.methodMap.get(methodName);
            if (method == null) {
                throw new LuaException("No such method " + methodName);
            }
            return method.apply(this.peripheral, context, wrappers.get(access), arguments);
        }
    }


    private class RemoteComputerWrapper implements IComputerAccess {
        private final IComputerAccess computer;
        private final PeripheralRecord record;

        RemoteComputerWrapper(IComputerAccess computer, PeripheralRecord record) {
            this.computer = computer;
            this.record = record;
        }

        public String mount(@Nonnull String desiredLocation, @Nonnull IMount mount) {
            return computer.mount(desiredLocation, mount, record.getName());
        }

        public String mount(@Nonnull String desiredLocation, @Nonnull IMount mount, @Nonnull String driveName) {
            return computer.mount(desiredLocation, mount, driveName);
        }

        public String mountWritable(@Nonnull String desiredLocation, @Nonnull IWritableMount mount) {
            return computer.mountWritable(desiredLocation, mount, record.getName());
        }

        public String mountWritable(@Nonnull String desiredLocation, @Nonnull IWritableMount mount, @Nonnull String driveName) {
            return computer.mountWritable(desiredLocation, mount, driveName);
        }

        public void unmount(String location) {
            computer.unmount(location);
        }

        public int getID() {
            return computer.getID();
        }

        public void queueEvent(@Nonnull String event, Object... arguments) {
            computer.queueEvent(event, arguments);
        }

        @Nonnull
        public IWorkMonitor getMainThreadMonitor() {
            return computer.getMainThreadMonitor();
        }

        @Nonnull
        public String getAttachmentName() {
            return record.getName();
        }

        @Nonnull
        public Map<String, IPeripheral> getAvailablePeripherals() {
            synchronized(peripheralsRecord) {
                //noinspection UnstableApiUsage
                return peripheralsRecord.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().getPeripheral()));
            }
        }

        @Nullable
        public IPeripheral getAvailablePeripheral(@Nonnull String name) {
            synchronized(peripheralsRecord) {
                PeripheralRecord record = peripheralsRecord.get(name);
                if (record != null)
                    return record.getPeripheral();
                return null;
            }
        }
    }

    private final ConcurrentMap<String, PeripheralRecord> peripheralsRecord = new ConcurrentHashMap<>();
    private final Set<IPeripheral> sharedPeripherals = Collections.newSetFromMap(new MapMaker().concurrencyLevel(4).weakKeys().makeMap());

    public <T extends TileEntity & IPeripheralTileEntity> BaseEnderwireModemPeripheral(T tileEntity) {
        super(TYPE, tileEntity);
    }

    public BaseEnderwireModemPeripheral(ITurtleAccess turtle, TurtleSide side) {
        super("modem", turtle, side);
    }

    public BaseEnderwireModemPeripheral(IPocketAccess access) {
        super("modem", access);
    }

    protected String buildElementType(@NotNull String networkName, @NotNull IPeripheral peripheral) {
        return networkName + "->" + peripheral.getType();
    }

    protected String selectName(@NotNull String networkName, @NotNull IPeripheral peripheral) {
        String elementType = buildElementType(networkName, peripheral);
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

    public void addSharedPeripheral(@NotNull String networkName, @NotNull IEnderwireNetworkElement element, @NotNull IPeripheral peripheral) {
        if (sharedPeripherals.contains(peripheral))
            return;
        String peripheralName = selectName(networkName, peripheral);
        PeripheralRecord record = new PeripheralRecord(peripheral, element.getName(), peripheralName);
        connectedComputers.forEach(record::attach);
        sharedPeripherals.add(peripheral);
        peripheralsRecord.put(peripheralName, record);
    }

    public void removeSharedPeripheral(@NotNull IEnderwireNetworkElement element) {
        Optional<PeripheralRecord> optRecord = peripheralsRecord.values().stream().filter(x -> x.getOwnerName().equals(element.getName())).findAny();
        optRecord.ifPresent(record -> {
            connectedComputers.forEach(record::detach);
            sharedPeripherals.remove(record.getPeripheral());
            peripheralsRecord.remove(record.getName());
        });
    }

    public void clearSharedPeripherals() {
        List<String> allKeys = new ArrayList<>(peripheralsRecord.keySet());
        allKeys.forEach(peripheralsRecord::remove);
    }

    @Override
    public void attach(@NotNull IComputerAccess computer) {
        super.attach(computer);
        peripheralsRecord.values().forEach(record -> record.attach(computer));
    }

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        super.detach(computer);
        peripheralsRecord.values().forEach(record -> record.detach(computer));
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final Collection<String> getNamesRemote() {
        return peripheralsRecord.keySet();
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final boolean isPresentRemote(String name) {
        return peripheralsRecord.containsKey(name);
    }

    @SuppressWarnings({"SameReturnValue", "unused"})
    @LuaFunction
    public final boolean isWireless() {
        return false;
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final Object[] getTypeRemote(IComputerAccess computer, String name) {
        PeripheralRecord record = peripheralsRecord.get(name);
        if (record == null)
            return null;
        return new Object[]{record.getPeripheral().getType()};
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final Object[] getMethodsRemote(IComputerAccess computer, String name) {
        PeripheralRecord record = peripheralsRecord.get(name);
        if (record == null)
            return null;
        return new Object[]{record.getMethodNames()};
    }

    @SuppressWarnings("unused")
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
