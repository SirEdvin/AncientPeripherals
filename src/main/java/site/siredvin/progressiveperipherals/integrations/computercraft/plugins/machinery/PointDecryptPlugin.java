package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.asm.TaskCallback;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryStructure;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.BasePlugin;

import java.util.Objects;

public class PointDecryptPlugin<T extends TileEntity & IMachineryController<T>> extends BasePlugin<T> {

    @Override
    public void buildMethodMap() {
        methods.put("isDecrypted", this::isDecrypted);
    }

    public @NotNull MethodResult isDecrypted(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return TaskCallback.make(context, () -> {
            World world = controllerEntity.getLevel();
            Objects.requireNonNull(world);
            IMachineryStructure structure = controllerEntity.getStructure();
            if (structure == null)
                return new Object[]{null, "Machine should be configured first"};
            TileEntity tileEntity = world.getBlockEntity(structure.getCenter());
            if (!(tileEntity instanceof RealityBreakthroughPointTileEntity))
                return new Object[]{null, "Cannot find breakthrough point"};
            return new Object[]{((RealityBreakthroughPointTileEntity) tileEntity).isDecrypted()};
        });
    }
}
