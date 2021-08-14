package site.siredvin.progressiveperipherals.extra.network.events;

import dan200.computercraft.api.peripheral.IPeripheral;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class EnderwireNetworkEvent implements IEnderwireBusEvent {
    private final long epoch;

    EnderwireNetworkEvent() {
        this.epoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public long getEpoch() {
        return epoch;
    }

    public boolean isValid() {
        return true;
    }

    private static class ElementEvent extends EnderwireNetworkEvent {
        private final WeakReference<EnderwireNetworkElement> element;

        public ElementEvent(EnderwireNetworkElement element) {
            super();
            this.element = new WeakReference<>(element);
        }

        public EnderwireNetworkElement getElement() {
            return element.get();
        }

        @Override
        public boolean isValid() {
            return element.get() != null;
        }
    }

    private static class PeripheralEvent extends ElementEvent {
        private final WeakReference<IPeripheral> peripheral;

        public PeripheralEvent(EnderwireNetworkElement element, IPeripheral peripheral) {
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

        public ElementAdded(EnderwireNetworkElement element) {
            super(element);
        }
    }

    public static class ElementRemoved extends ElementEvent {

        public ElementRemoved(EnderwireNetworkElement element) {
            super(element);
        }
    }

    public static class PeripheralAttached extends PeripheralEvent {

        public PeripheralAttached(EnderwireNetworkElement element, IPeripheral peripheral) {
            super(element, peripheral);
        }
    }

    public static class PeripheralDetached extends PeripheralEvent {

        public PeripheralDetached(EnderwireNetworkElement element, IPeripheral peripheral) {
            super(element, peripheral);
        }
    }
}
