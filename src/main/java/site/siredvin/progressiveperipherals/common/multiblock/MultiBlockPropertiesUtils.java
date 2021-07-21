package site.siredvin.progressiveperipherals.common.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.common.setup.Blocks;

public class MultiBlockPropertiesUtils {
    public static BlockState setFacing(BlockState state, Direction direction) {
        if (state.is(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get()))
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        return state.setValue(BlockStateProperties.FACING, direction);
    }

    public static BlockState setConnected(BlockState state, boolean isConnected) {
        return state.setValue(MultiBlockProperties.CONNECTED, isConnected);
    }
}
