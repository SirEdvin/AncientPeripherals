package site.siredvin.progressiveperipherals.api.blocks;

import net.minecraft.tileentity.TileEntity;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;

import java.util.Map;

public interface IPeripheralPlugin<T extends TileEntity> {
    Map<String, IPluggableLuaMethod<T>> getMethods();
}
