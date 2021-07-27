package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import site.siredvin.progressiveperipherals.common.machinery.MachineryBlockProperties;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.StatueWorkbenchTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

public class StatueWorkbench extends GenericTileEntityBlock<StatueWorkbenchTileEntity> {
    private static final BooleanProperty CONNECTED = MachineryBlockProperties.CONNECTED;

    public StatueWorkbench() {
        super(TileEntityTypes.STATUE_WORKBENCH, BlockUtils.defaultProperties());
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }
}
