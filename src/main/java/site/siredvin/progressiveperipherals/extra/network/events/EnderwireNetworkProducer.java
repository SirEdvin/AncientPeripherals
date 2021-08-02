package site.siredvin.progressiveperipherals.extra.network.events;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;

public class EnderwireNetworkProducer {
    public static void firePoweredEvent(BlockState oldState, BlockState newState, World world, BlockPos pos, EnderwireComputerEventType disabledEvent, EnderwireComputerEventType enabledEvent) {
        if (newState.getBlock().is(oldState.getBlock())) {
            boolean wasPowered = oldState.getValue(BlockStateProperties.POWERED);
            boolean isPowered = newState.getValue(BlockStateProperties.POWERED);
            if (!world.isClientSide && wasPowered != isPowered) {
                IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
                if (te != null) {
                    String attachedNetwork = te.getAttachedNetwork();
                    if (attachedNetwork != null) {
                        EnderwireComputerEventType eventName = enabledEvent;
                        if (!isPowered)
                            eventName = disabledEvent;
                        EnderwireNetworkBusHub.fireComputerEvent(attachedNetwork, EnderwireComputerEvent.timed(eventName, te.getElementUUID().toString()));
                    }
                }
            }
        }
    }

    public static void firePoweredLeverEvent(BlockState oldState, BlockState newState, World world, BlockPos pos) {
        firePoweredEvent(oldState, newState, world, pos, EnderwireComputerEventType.LEVER_DISABLED, EnderwireComputerEventType.LEVER_ENABLED);
    }

    public static void firePoweredButtonEvent(BlockState oldState, BlockState newState, World world, BlockPos pos) {
        firePoweredEvent(oldState, newState, world, pos, EnderwireComputerEventType.BUTTON_DISABLED, EnderwireComputerEventType.BUTTON_ENABLED);
    }
}
