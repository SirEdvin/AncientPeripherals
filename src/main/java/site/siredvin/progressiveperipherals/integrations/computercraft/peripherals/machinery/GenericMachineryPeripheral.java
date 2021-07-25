package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import de.srendi.advancedperipherals.common.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;

public abstract class GenericMachineryPeripheral<T extends PeripheralTileEntity<?> & IMachineryController<T>> extends OperationPeripheral implements IDynamicPeripheral {
    private final T tileEntity;
    private boolean waitForInvalidateCheck = false;

    public GenericMachineryPeripheral(String type, T tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @NotNull
    @Override
    public String[] getMethodNames() {
        return tileEntity.getMethodNames();
    }

    @NotNull
    @Override
    public MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int methodIndex, @NotNull IArguments arguments) throws LuaException {
        return tileEntity.callMethod(access, context, methodIndex, arguments);
    }

    public void prepareInvalidateCheck() {
        this.waitForInvalidateCheck = true;
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

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        if (waitForInvalidateCheck && iPeripheral == this) {
            waitForInvalidateCheck = false;
            return false;
        }
        return super.equals(iPeripheral);
    }
}
