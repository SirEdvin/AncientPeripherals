package site.siredvin.progressiveperipherals.common.machinery;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.utils.ValueContainer;

import java.util.function.Predicate;

public class MachineryBlockProperties {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public static final Predicate<BlockState> NOT_CONNECTED_TO_ANYTHING = state -> !state.getValue(CONNECTED);
    public static final Predicate<BlockState> CONNECTED_TO_ANYTHING = NOT_CONNECTED_TO_ANYTHING.negate();

    public static BlockState setFacing(BlockState state, Direction direction) {
        ValueContainer<BlockState> container = ValueContainer.of(state);
        state.getValues().keySet().stream()
                .filter(p -> p.getName().equals("facing")).findAny()
                .ifPresent(p -> container.mutate(bState -> bState.setValue((DirectionProperty) p, direction)));
        return container.getValue();
    }

    public static BlockState setConnected(BlockState state, boolean isConnected) {
        return state.setValue(CONNECTED, isConnected);
    }
}
