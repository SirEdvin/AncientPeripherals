package site.siredvin.progressiveperipherals.api.multiblock;

import net.minecraft.tileentity.TileEntity;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;

public interface IMultiBlockPlugin<T extends TileEntity & IMultiBlockController> {
    MultiBlockPluginType getType();
    IPeripheralPlugin<T> getPlugin();
}
