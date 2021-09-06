package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TileEntityPeripheralOwner;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.common.tileentities.base.OptionalPeripheralTileEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericMachineryPeripheral<T extends OptionalPeripheralTileEntity<?> & IMachineryController<T>> extends BasePeripheral<TileEntityPeripheralOwner<T>> implements IDynamicPeripheral {
    private final Map<String, IPluggableLuaMethod<T>> methodMap;
    private final String[] methodNames;

    public GenericMachineryPeripheral(String type, T tileEntity) {
        super(type, new TileEntityPeripheralOwner<>(tileEntity));
        owner.attachOperation();
        methodMap = new HashMap<>();
        for (IPeripheralPlugin<T> plugin: owner.tileEntity.getPlugins()) {
            methodMap.putAll(plugin.getMethods());
        }
        methodNames = methodMap.keySet().toArray(new String[0]);
    }

    @NotNull
    @Override
    public String @NotNull [] getMethodNames() {
        return methodNames;
    }

    @NotNull
    @Override
    public MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int methodIndex, @NotNull IArguments arguments) throws LuaException {
        IPluggableLuaMethod<T> method = methodMap.get(getMethodNames()[methodIndex]);
        if (method == null)
            throw new IllegalArgumentException("Cannot find method ...");
        return method.call(access, context, arguments, owner.tileEntity);
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final boolean isConnected() {
        return owner.tileEntity.isConfigured();
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult connect() {
        Pair<Boolean, String> result = owner.tileEntity.detectMultiBlock();
        return MethodResult.of(result.getLeft(), result.getRight());
    }
}
