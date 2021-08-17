package site.siredvin.progressiveperipherals.extra.network.events;

import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkStats;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;

import java.lang.ref.WeakReference;

public class EnderwireNetworkEvent implements IEnderwireBusEvent {

    EnderwireNetworkEvent() {}

    public boolean isValid() {
        return true;
    }

    private static class ElementEvent extends EnderwireNetworkEvent {
        private final WeakReference<IEnderwireNetworkElement> element;

        public ElementEvent(@NotNull IEnderwireNetworkElement element) {
            super();
            this.element = new WeakReference<>(element);
        }

        public IEnderwireNetworkElement getElement() {
            return element.get();
        }

        @Override
        public boolean isValid() {
            return element.get() != null;
        }
    }

    private static class NetworkEvent extends EnderwireNetworkEvent {
        private final WeakReference<EnderwireNetwork> network;

        public NetworkEvent(@NotNull EnderwireNetwork network) {
            super();
            this.network = new WeakReference<>(network);
        }

        public EnderwireNetwork getNetwork() {
            return network.get();
        }

        @Override
        public boolean isValid() {
            return network.get() != null;
        }
    }

    private static class PeripheralEvent extends ElementEvent {
        private final WeakReference<IPeripheral> peripheral;

        public PeripheralEvent(@NotNull IEnderwireNetworkElement element, @NotNull IPeripheral peripheral) {
            super(element);
            this.peripheral = new WeakReference<>(peripheral);
        }

        public IPeripheral getPeripheral() {
            return peripheral.get();
        }

        @Override
        public boolean isValid() {
            return super.isValid() && peripheral.get() != null;
        }
    }

    public static class ElementAdded extends ElementEvent {

        public ElementAdded(@NotNull IEnderwireNetworkElement element) {
            super(element);
        }
    }

    public static class ElementRemoved extends ElementEvent {

        public ElementRemoved(@NotNull IEnderwireNetworkElement element) {
            super(element);
        }
    }

    public static class PeripheralAttached extends PeripheralEvent {

        public PeripheralAttached(@NotNull IEnderwireNetworkElement element, @NotNull IPeripheral peripheral) {
            super(element, peripheral);
        }
    }

    public static class PeripheralDetached extends PeripheralEvent {

        public PeripheralDetached(@NotNull IEnderwireNetworkElement element, @NotNull IPeripheral peripheral) {
            super(element, peripheral);
        }
    }

    public static class NetworkStatsChanged extends NetworkEvent {

        private final @NotNull EnderwireNetworkStats oldStats;
        private final @NotNull EnderwireNetworkStats newStats;

        public NetworkStatsChanged(@NotNull EnderwireNetwork network, @NotNull EnderwireNetworkStats oldStats, @NotNull EnderwireNetworkStats newStats) {
            super(network);
            this.oldStats = oldStats;
            this.newStats = newStats;
        }

        public @NotNull EnderwireNetworkStats getOldStats() {
            return oldStats;
        }

        public @NotNull EnderwireNetworkStats getNewStats() {
            return newStats;
        }
    }
}
