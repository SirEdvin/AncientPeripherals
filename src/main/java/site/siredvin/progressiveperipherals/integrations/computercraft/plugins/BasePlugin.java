package site.siredvin.progressiveperipherals.integrations.computercraft.plugins;

import net.minecraft.tileentity.TileEntity;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;

import java.util.HashMap;
import java.util.Map;

public abstract class BasePlugin<T extends TileEntity> implements IPeripheralPlugin<T> {
    protected final Map<String, IPluggableLuaMethod<T>> methods;

    public BasePlugin() {
        methods = new HashMap<>();
        buildMethodMap();
    }

    public abstract void buildMethodMap();

    @Override
    public Map<String, IPluggableLuaMethod<T>> getMethods() {
        return methods;
    }
}
