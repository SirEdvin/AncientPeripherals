package site.siredvin.progressiveperipherals.common.machinery;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.common.setup.Blocks;

import java.util.function.Predicate;

public class MachineryBlockProperties {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public static final Predicate<BlockState> NOT_CONNECTED_TO_ANYTHING = state -> !state.getValue(CONNECTED);
    public static final Predicate<BlockState> CONNECTED_TO_ANYTHING = NOT_CONNECTED_TO_ANYTHING.negate();

    public static BlockState setFacing(BlockState state, Direction direction) {
        if (state.is(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get()))
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        return state.setValue(BlockStateProperties.FACING, direction);
    }

    public static BlockState setConnected(BlockState state, boolean isConnected) {
        return state.setValue(CONNECTED, isConnected);
    }
}
