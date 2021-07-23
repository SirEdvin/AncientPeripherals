package site.siredvin.progressiveperipherals.common.blocks.multiblock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockPlugin;
import site.siredvin.progressiveperipherals.api.multiblock.MultiBlockPluggableFeature;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class MultiBlockPlugin extends MultiBlockBlock implements IMultiBlockPlugin<RealityBreakthroughRectorControllerTileEntity> {

    private final @Nullable IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin;
    private final @Nullable MultiBlockPluggableFeature type;

    public MultiBlockPlugin(Properties properties, @Nullable IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin) {
        super(properties);
        this.plugin = plugin;
        this.type = null;
    }

    public MultiBlockPlugin(Properties properties, @Nullable IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin, @NotNull MultiBlockPluggableFeature type) {
        super(properties);
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public @Nullable MultiBlockPluggableFeature getFeature() {
        return type;
    }

    @Override
    public @Nullable IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> getPlugin() {
        return plugin;
    }
}
