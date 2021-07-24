package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;

import java.util.Collections;
import java.util.List;

public class RBTExtractorPeripheral  extends OperationPeripheral implements IDynamicPeripheral {
    private final RBTExtractorControllerTileEntity tileEntity;
    public RBTExtractorPeripheral(String type, RBTExtractorControllerTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(FreeMachineryOperation.EXTRACT);
    }
}
