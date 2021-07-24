package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.rbtmachinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.asm.TaskCallback;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;

import java.util.HashMap;
import java.util.Map;

public class ControllerPlugin<T extends TileEntity & IMachineryController<T>> implements IPeripheralPlugin<T> {
    private final Map<String, IPluggableLuaMethod<T>> methods;

    public ControllerPlugin() {
        methods = new HashMap<>();
        methods.put("isConnected", this::isConnected);
        methods.put("connect", this::connect);
    }

    public @NotNull MethodResult isConnected(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) {
        return MethodResult.of(controllerEntity.isConfigured());
    }

    public @NotNull MethodResult connect(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return TaskCallback.make(context, () -> {
            Pair<Boolean, String> result = controllerEntity.detectMultiBlock();
            return new Object[]{MethodResult.of(result.getLeft(), result.getRight())};
        });
    }

    @Override
    public Map<String, IPluggableLuaMethod<T>> getMethods() {
        return methods;
    }
}
