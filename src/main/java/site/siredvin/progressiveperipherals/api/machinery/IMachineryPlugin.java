package site.siredvin.progressiveperipherals.api.machinery;

import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;

public interface IMachineryPlugin<T extends TileEntity & IMachineryController<T>> {
    @NotNull IPeripheralPlugin<T> getPlugin();
}
