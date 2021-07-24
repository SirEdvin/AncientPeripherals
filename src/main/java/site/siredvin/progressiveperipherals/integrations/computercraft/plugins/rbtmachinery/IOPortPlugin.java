package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.rbtmachinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;

import java.util.HashMap;
import java.util.Map;

public class IOPortPlugin implements IPeripheralPlugin<RBTRectorControllerTileEntity> {

    private final Map<String, IPluggableLuaMethod<RBTRectorControllerTileEntity>> methods;

    public IOPortPlugin() {
        methods = new HashMap<>();
        methods.put("curcuma", this::curcuma);
    }

    protected @NotNull MethodResult curcuma(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull RBTRectorControllerTileEntity controllerEntity) {
        return MethodResult.of("Curcuma");
    }

    @Override
    public Map<String, IPluggableLuaMethod<RBTRectorControllerTileEntity>> getMethods() {
        return methods;
    }
}
