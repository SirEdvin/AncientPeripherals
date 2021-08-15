package site.siredvin.progressiveperipherals.extra.network.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnderwireNetworkBusHub {

    private static final Map<String, EnderwireEventBus<EnderwireNetworkEvent>> networkEvents = new HashMap<>();
    private static final Map<String, EnderwireEventBus<EnderwireComputerEvent>> computerEvents = new HashMap<>();

    private static EnderwireEventBus<EnderwireNetworkEvent> getNetworkEventBus(@NotNull String networkName) {
        if (!networkEvents.containsKey(networkName))
            networkEvents.put(networkName, new EnderwireEventBus<>());
        return networkEvents.get(networkName);
    }

    private static EnderwireEventBus<EnderwireComputerEvent> getComputerEventBus(@NotNull String networkName) {
        if (!computerEvents.containsKey(networkName))
            computerEvents.put(networkName, new EnderwireEventBus<>());
        return computerEvents.get(networkName);
    }

    public static void fireNetworkEvent(@NotNull String networkName, EnderwireNetworkEvent event) {
        getNetworkEventBus(networkName).handleEvent(event);
    }

    public static EnderwireEventSubscription<EnderwireNetworkEvent> subscribeToNetworkEvents(@NotNull String networkName, IEnderwireEventConsumer<EnderwireNetworkEvent> consumer) {
        return getNetworkEventBus(networkName).subscribe(consumer);
    }

    public static void unsubscribeFromNetworkEvents(@NotNull String networkName, EnderwireEventSubscription<EnderwireNetworkEvent> subscription) {
        getNetworkEventBus(networkName).unsubscribe(subscription);
    }

    public static void fireComputerEvent(@NotNull String networkName, EnderwireComputerEvent event) {
        getComputerEventBus(networkName).handleEvent(event);
    }

    public static EnderwireEventSubscription<EnderwireComputerEvent> subscribeToComputerEvents(@NotNull String networkName, IEnderwireEventConsumer<EnderwireComputerEvent> consumer) {
        return getComputerEventBus(networkName).subscribe(consumer);
    }

    public static void unsubscribeFromComputerEvents(@NotNull String networkName, EnderwireEventSubscription<EnderwireComputerEvent> subscription) {
        getComputerEventBus(networkName).unsubscribe(subscription);
    }

    @SubscribeEvent
    public static void afterServerStopped(FMLServerStoppedEvent event) {
        networkEvents.clear();
        computerEvents.clear();
    }

    @SubscribeEvent
    public static void beforeServerStarted(FMLServerStartingEvent event) {
        networkEvents.clear();
        computerEvents.clear();
    }
}

