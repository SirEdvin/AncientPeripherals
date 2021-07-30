package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;

public class CreativePowerGeneratorPlugin <T extends TileEntity & IMachineryController<T>> extends BreakthroughPointPlugin<T> {
    @Override
    public void buildMethodMap() {
        methods.put("addPowerToPoint", this::addPowerToPoint);
    }

    public @NotNull MethodResult addPowerToPoint(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        int amount = arguments.optInt(0, 1000);
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> {
            point.consumePower(-amount);
            return new Object[]{true};
        });
    }
}
