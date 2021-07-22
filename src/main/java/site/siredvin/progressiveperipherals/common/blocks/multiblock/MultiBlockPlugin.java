package site.siredvin.progressiveperipherals.common.blocks.multiblock;

import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockPlugin;
import site.siredvin.progressiveperipherals.api.multiblock.MultiBlockPluginType;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class MultiBlockPlugin extends MultiBlockBlock implements IMultiBlockPlugin<RealityBreakthroughRectorControllerTileEntity> {

    private final IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin;
    private final MultiBlockPluginType type;

    public MultiBlockPlugin(Properties properties, IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin) {
        super(properties);
        this.plugin = plugin;
        this.type = MultiBlockPluginType.COMMON;
    }

    public MultiBlockPlugin(Properties properties, IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin, MultiBlockPluginType type) {
        super(properties);
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public MultiBlockPluginType getType() {
        return type;
    }

    @Override
    public IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> getPlugin() {
        return plugin;
    }
}
