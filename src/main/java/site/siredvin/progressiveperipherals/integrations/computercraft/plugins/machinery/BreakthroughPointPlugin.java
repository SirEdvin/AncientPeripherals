package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.core.asm.TaskCallback;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryStructure;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.BasePlugin;

import java.util.Objects;
import java.util.function.BiFunction;

public abstract class BreakthroughPointPlugin<T extends TileEntity & IMachineryController<T>> extends BasePlugin<T> {
    public @NotNull MethodResult withBreakthroughPoint(@NotNull ILuaContext context, @NotNull T controllerEntity, BiFunction<RealityBreakthroughPointTileEntity, World, Object[]> function) throws LuaException {
        return TaskCallback.make(context, () -> {
            World world = controllerEntity.getLevel();
            Objects.requireNonNull(world);
            IMachineryStructure structure = controllerEntity.getStructure();
            if (structure == null)
                return new Object[]{null, "Machine should be configured first"};
            TileEntity tileEntity = world.getBlockEntity(structure.getCenter());
            if (!(tileEntity instanceof RealityBreakthroughPointTileEntity))
                return new Object[]{null, "Cannot find breakthrough point"};
            return function.apply((RealityBreakthroughPointTileEntity) tileEntity, world);
        });
    }
}
