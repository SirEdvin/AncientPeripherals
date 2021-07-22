package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.rbtreactor;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.blocks.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

import java.util.HashMap;
import java.util.Map;

public class IOPortPlugin implements IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> {

    private final Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> methods;

    public IOPortPlugin() {
        methods = new HashMap<>();
        methods.put("curcuma", this::curcuma);
    }

    protected @NotNull MethodResult curcuma(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull RealityBreakthroughRectorControllerTileEntity controllerEntity) {
        return MethodResult.of("Curcuma");
    }

    @Override
    public Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> getMethods() {
        return methods;
    }
}
