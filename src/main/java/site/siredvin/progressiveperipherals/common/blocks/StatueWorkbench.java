package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.extra.machinery.MachineryBlockProperties;
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

    @Override
    public void neighborChanged(@NotNull BlockState state, World world, @NotNull BlockPos pos, @NotNull Block neighbourBlock, @NotNull BlockPos neighbourPos, boolean isMoving) {
        if (pos.above().equals(neighbourPos)) {
            boolean isFlexibleStatue = world.getBlockState(neighbourPos).is(Blocks.FLEXIBLE_STATUE.get());
            if (state.getValue(CONNECTED) && !isFlexibleStatue)
                world.setBlock(pos, state.setValue(MachineryBlockProperties.CONNECTED, false), Constants.BlockFlags.DEFAULT);
            if (!state.getValue(CONNECTED) && isFlexibleStatue)
                world.setBlock(pos, state.setValue(MachineryBlockProperties.CONNECTED, true), Constants.BlockFlags.DEFAULT);
        }
        super.neighborChanged(state, world, pos, neighbourBlock, neighbourPos, isMoving);
    }
}
