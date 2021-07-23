package site.siredvin.progressiveperipherals.api.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;

public interface IMultiBlockPlugin<T extends TileEntity & IMultiBlockController<T>> {
    @Nullable MultiBlockPluggableFeature getFeature();
    @Nullable IPeripheralPlugin<T> getPlugin();
}
