package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;

public class CreativeDecryptPlugin<T extends TileEntity & IMachineryController<T>> extends BreakthroughPointPlugin<T> {
    @Override
    public void buildMethodMap() {
        methods.put("forceDecrypt", this::forceDecrypt);
    }

    public @NotNull MethodResult forceDecrypt(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> {
            while (!point.isDecrypted())
                point.decryptLevel();
            return new Object[]{true};
        });
    }
}
