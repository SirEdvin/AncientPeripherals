package site.siredvin.progressiveperipherals.extra.network.events;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireRedstoneSensorBlock;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireRedstoneSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;
import site.siredvin.progressiveperipherals.utils.ExtraLuaConverter;
import site.siredvin.progressiveperipherals.utils.OrientationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class EnderwireNetworkEventProducer {

    protected static void eventEnvironment(@NotNull World world, @NotNull BlockPos pos, BiConsumer<IEnderwireElement, EnderwireNetwork> consumer) {
       eventEnvironment(world, pos, IEnderwireElement.class, consumer);
    }

    protected static <T extends IEnderwireElement> void eventEnvironment(@NotNull World world, @NotNull BlockPos pos, Class<T> entityClass, BiConsumer<T, EnderwireNetwork> consumer) {
        eventEnvironment(world, pos, entityClass, null, consumer);
    }

    protected static void eventEnvironment(@NotNull World world, @NotNull BlockPos pos, @Nullable String attachedNetwork, BiConsumer<IEnderwireElement, EnderwireNetwork> consumer) {
        eventEnvironment(world, pos, IEnderwireElement.class, attachedNetwork, consumer);
    }

    protected static <T extends IEnderwireElement> void eventEnvironment(@NotNull World world, @NotNull BlockPos pos, Class<T> entityClass, @Nullable String attachedNetwork, BiConsumer<T, EnderwireNetwork> consumer) {
        if (!world.isClientSide) {
            TileEntity te = world.getBlockEntity(pos);
            if (entityClass.isInstance(te)) {
                T castedEntity = entityClass.cast(te);
                if (attachedNetwork == null)
                    attachedNetwork = castedEntity.getAttachedNetwork();
                GlobalNetworksData networksData = GlobalNetworksData.get((ServerWorld) world);
                EnderwireNetwork currentNetwork = networksData.getNetwork(attachedNetwork);
                if (attachedNetwork != null && currentNetwork != null) {
                    consumer.accept(castedEntity, currentNetwork);
                }
            }
        }
    }

    public static void firePoweredEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @NotNull EnderwireElementType component, @Nullable PlayerEntity player, boolean extendedData) {
        eventEnvironment(world, pos, (te, currentNetwork) -> {
            String eventName = component.getEnabledEventName();
            if (!isEnabled)
                eventName = component.getDisabledEventName();
            Map<String, Object> data = new HashMap<>();
            data.put("element", te.getElementName());
            if (extendedData && player != null)
                data.put("player", player.getName().getString());
            EnderwireNetworkBusHub.fireComputerEvent(currentNetwork.getName(), EnderwireComputerEvent.timed(
                    eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                    world.dimension().location().toString(), pos, data
            ));
        });
    }

    public static void firePoweredLeverEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @Nullable PlayerEntity player, boolean extendedData) {
        firePoweredEvent(isEnabled, world, pos, EnderwireElementType.LEVER, player, extendedData);
    }

    public static void firePoweredButtonEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @Nullable PlayerEntity player, boolean extendedData) {
        firePoweredEvent(isEnabled, world, pos, EnderwireElementType.BUTTON, player, extendedData);
    }

    public static void firePoweredPlateEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @Nullable List<? extends Entity> collidingEntities, boolean extendedData) {
        eventEnvironment(world, pos, (te, currentNetwork) -> {
            String eventName = EnderwireElementType.PLATE.getEnabledEventName();
            if (!isEnabled)
                eventName = EnderwireElementType.PLATE.getDisabledEventName();
            Map<String, Object> data = new HashMap<>();
            data.put("element", te.getElementName());
            if (extendedData && collidingEntities != null)
                data.put("entities", collidingEntities.stream().map(ExtraLuaConverter::classifyEntity).collect(Collectors.toList()));
            EnderwireNetworkBusHub.fireComputerEvent(currentNetwork.getName(), EnderwireComputerEvent.timed(
                    eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                    world.dimension().location().toString(), pos, data
            ));
        });
    }

    public static void fireRedstoneSensorEvent(int signal, Direction neighbor, @NotNull World world, @NotNull BlockPos pos) {
        eventEnvironment(world, pos, EnderwireRedstoneSensorTileEntity.class, (te, currentNetwork) -> {
            String eventName = EnderwireElementType.REDSTONE_SENSOR.getChangedEventName();
            BlockState state = world.getBlockState(pos);
            Map<String, Object> data = new HashMap<>();
            data.put("element", te.getElementName());
            data.put("side", OrientationUtils.toSide(
                    state.getValue(EnderwireRedstoneSensorBlock.FACING),
                    state.getValue(EnderwireRedstoneSensorBlock.FACE),
                    neighbor
            ).name().toLowerCase());
            data.put("power", signal);
            EnderwireNetworkBusHub.fireComputerEvent(currentNetwork.getName(), EnderwireComputerEvent.timed(
                    eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                    world.dimension().location().toString(), pos, data
            ));
        });
    }

    public static void fireElementAttachedEvent(@NotNull World world, @NotNull BlockPos pos, String attachedNetwork) {
        eventEnvironment(world, pos, attachedNetwork, (te, currentNetwork) -> {
            String eventName = te.getElementType().getAttachEventName();
            IEnderwireNetworkElement element = currentNetwork.getElement(te.getElementName());
            Objects.requireNonNull(element);
            Map<String, Object> data = NetworkRepresentationTool.elementRepresentation(element);
            EnderwireNetworkBusHub.fireComputerEvent(currentNetwork.getName(), EnderwireComputerEvent.timed(
                    eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                    world.dimension().location().toString(), pos, data
            ));
        });
    }

    public static void fireElementDetachedEvent(@NotNull World world, @NotNull BlockPos pos, String oldNetwork) {
        eventEnvironment(world, pos, oldNetwork, (te, currentNetwork) -> {
            String eventName = te.getElementType().getDetachEventName();
            IEnderwireNetworkElement element = currentNetwork.getElement(te.getElementName());
            Objects.requireNonNull(element);
            Map<String, Object> data = NetworkRepresentationTool.elementRepresentation(element);
            EnderwireNetworkBusHub.fireComputerEvent(currentNetwork.getName(), EnderwireComputerEvent.timed(
                    eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                    world.dimension().location().toString(), pos, data
            ));
        });
    }
}
