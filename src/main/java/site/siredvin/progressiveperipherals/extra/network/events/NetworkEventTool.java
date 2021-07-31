package site.siredvin.progressiveperipherals.extra.network.events;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NetworkEventTool {
    private static final Map<String, EnderwireEventBus<EnderwireNetworkEvent>> networkEvents = new HashMap<>();
    private static final Map<String, EnderwireEventBus<EnderwireComputerEvent>> computerEvents = new HashMap<>();

    public static void fireNetworkEvent(String networkName, EnderwireNetworkEvent event) {
        if (!networkEvents.containsKey(networkName))
            networkEvents.put(networkName, new EnderwireEventBus<>());
        networkEvents.get(networkName).handleEvent(event);
    }

    public static long traverseNetworkEvents(String networkName, long lastConsumedMessage, Consumer<EnderwireNetworkEvent> consumer) {
        EnderwireEventBus<EnderwireNetworkEvent> bus = networkEvents.get(networkName);
        if (bus != null)
            return bus.traverse(lastConsumedMessage, consumer);
        return lastConsumedMessage;
    }

    public static long getNetworkEventsStart(String networkName) {
        EnderwireEventBus<EnderwireNetworkEvent> bus = networkEvents.get(networkName);
        if (bus == null)
            return -1;
        return bus.getCounter() - 1;
    }

    public static void fireComputerEvent(String networkName, EnderwireComputerEvent event) {
        if (!computerEvents.containsKey(networkName))
            computerEvents.put(networkName, new EnderwireEventBus<>());
        computerEvents.get(networkName).handleEvent(event);
    }

    public static long traverseComputerEvents(String networkName, long lastConsumedMessage, Consumer<EnderwireComputerEvent> consumer) {
        EnderwireEventBus<EnderwireComputerEvent> bus = computerEvents.get(networkName);
        if (bus != null)
            return bus.traverse(lastConsumedMessage, consumer);
        return lastConsumedMessage;
    }

    public static long getComputerEventsStart(String networkName) {
        EnderwireEventBus<EnderwireComputerEvent> bus = computerEvents.get(networkName);
        if (bus == null)
            return -1;
        return bus.getCounter() - 1;
    }
}
