package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.rbtreactor;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.asm.TaskCallback;
import de.srendi.advancedperipherals.common.util.Pair;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.blocks.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

import java.util.HashMap;
import java.util.Map;

public class ControllerPlugin implements IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> {
    private final Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> methods;

    public ControllerPlugin() {
        methods = new HashMap<>();
        methods.put("isConnected", this::isConnected);
        methods.put("connect", this::connect);
    }

    public @NotNull MethodResult isConnected(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull RealityBreakthroughRectorControllerTileEntity controllerEntity) {
        return MethodResult.of(controllerEntity.isConfigured());
    }

    public @NotNull MethodResult connect(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull RealityBreakthroughRectorControllerTileEntity controllerEntity) throws LuaException {
        return TaskCallback.make(context, () -> {
            Pair<Boolean, String> result = controllerEntity.detectMultiBlock();
            return new Object[]{MethodResult.of(result.getLeft(), result.getRight())};
        });
    }

    @Override
    public Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> getMethods() {
        return methods;
    }
}
