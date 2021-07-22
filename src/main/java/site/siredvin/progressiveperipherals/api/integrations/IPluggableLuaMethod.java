package site.siredvin.progressiveperipherals.api.integrations;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IPluggableLuaMethod<T extends TileEntity> {
    @NotNull MethodResult call(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException;
}
