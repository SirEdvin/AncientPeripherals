package site.siredvin.progressiveperipherals.common.blocks.machinery;

import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryPlugin;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTRectorControllerTileEntity;

public class MachineryPluggableBlock extends MachineryBlock implements IMachineryPlugin<RBTRectorControllerTileEntity> {

    private final @NotNull IPeripheralPlugin<RBTRectorControllerTileEntity> plugin;

    public MachineryPluggableBlock(Properties properties, @NotNull IPeripheralPlugin<RBTRectorControllerTileEntity> plugin) {
        super(properties);
        this.plugin = plugin;
    }

    @Override
    public @NotNull IPeripheralPlugin<RBTRectorControllerTileEntity> getPlugin() {
        return plugin;
    }
}
