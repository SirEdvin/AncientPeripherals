package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.BasePlugin;

public class IOPortPlugin<T extends TileEntity & IMachineryController<T>> extends BasePlugin<T> {

    @Override
    public void buildMethodMap() {
        methods.put("curcuma", this::curcuma);
    }

    protected @NotNull MethodResult curcuma(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) {
        return MethodResult.of("Curcuma");
    }
}
