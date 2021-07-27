package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import de.srendi.advancedperipherals.common.util.Pair;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericMachineryPeripheral<T extends PeripheralTileEntity<?> & IMachineryController<T>> extends OperationPeripheral implements IDynamicPeripheral {
    protected final T tileEntity;
    private final Map<String, IPluggableLuaMethod<T>> methodMap;
    private final String[] methodNames;

    public GenericMachineryPeripheral(String type, T tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
        methodMap = new HashMap<>();
        for (IPeripheralPlugin<T> plugin: this.tileEntity.getPlugins()) {
            methodMap.putAll(plugin.getMethods());
        }
        methodNames = methodMap.keySet().toArray(new String[0]);
    }

    @NotNull
    @Override
    public String[] getMethodNames() {
        return methodNames;
    }

    @NotNull
    @Override
    public MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int methodIndex, @NotNull IArguments arguments) throws LuaException {
        IPluggableLuaMethod<T> method = methodMap.get(getMethodNames()[methodIndex]);
        if (method == null)
            throw new IllegalArgumentException("Cannot find method ...");
        return method.call(access, context, arguments, tileEntity);
    }

    @LuaFunction
    public boolean isConnected() {
        return tileEntity.isConfigured();
    }

    @LuaFunction(mainThread = true)
    public MethodResult connect() {
        Pair<Boolean, String> result = tileEntity.detectMultiBlock();
        return MethodResult.of(result.getLeft(), result.getRight());
    }
}
