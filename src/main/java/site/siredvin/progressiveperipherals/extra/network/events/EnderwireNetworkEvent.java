package site.siredvin.progressiveperipherals.extra.network.events;

import dan200.computercraft.api.peripheral.IPeripheral;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;

import java.lang.ref.WeakReference;

public class EnderwireNetworkEvent implements IEnderwireBusEvent {

    EnderwireNetworkEvent() {}

    public boolean isValid() {
        return true;
    }

    private static class ElementEvent extends EnderwireNetworkEvent {
        private final WeakReference<IEnderwireNetworkElement> element;

        public ElementEvent(IEnderwireNetworkElement element) {
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

    private static class PeripheralEvent extends ElementEvent {
        private final WeakReference<IPeripheral> peripheral;

        public PeripheralEvent(IEnderwireNetworkElement element, IPeripheral peripheral) {
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

        public ElementAdded(IEnderwireNetworkElement element) {
            super(element);
        }
    }

    public static class ElementRemoved extends ElementEvent {

        public ElementRemoved(IEnderwireNetworkElement element) {
            super(element);
        }
    }

    public static class PeripheralAttached extends PeripheralEvent {

        public PeripheralAttached(IEnderwireNetworkElement element, IPeripheral peripheral) {
            super(element, peripheral);
        }
    }

    public static class PeripheralDetached extends PeripheralEvent {

        public PeripheralDetached(IEnderwireNetworkElement element, IPeripheral peripheral) {
            super(element, peripheral);
        }
    }
}
