package site.siredvin.progressiveperipherals.common.blocks.rbtreactor;

import site.siredvin.progressiveperipherals.api.blocks.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

import java.util.Map;

public class RealityBreakthroughReactorPlugin extends RealityBreakthroughReactorBlock implements IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> {

    private final IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin;

    public RealityBreakthroughReactorPlugin(Properties properties, IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin) {
        super(properties);
        this.plugin = plugin;
    }

    @Override
    public Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> getMethods() {
        return plugin.getMethods();
    }
}
