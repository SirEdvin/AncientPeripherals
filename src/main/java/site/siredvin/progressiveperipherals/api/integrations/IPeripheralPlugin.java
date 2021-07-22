package site.siredvin.progressiveperipherals.api.integrations;

import net.minecraft.tileentity.TileEntity;

import java.util.Map;

public interface IPeripheralPlugin<T extends TileEntity> {
    Map<String, IPluggableLuaMethod<T>> getMethods();
}
