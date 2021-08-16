package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementCategory {
    SENSOR(),
    SERVICE(),
    PROPAGATOR(true),
    MODIFICATION(),
    EMITTER();

    private final boolean canSharePeripheral;

    @SuppressWarnings("SameParameterValue")
    EnderwireElementCategory(boolean canSharePeripheral) {
        this.canSharePeripheral = canSharePeripheral;
    }

    EnderwireElementCategory() {
        this.canSharePeripheral = false;
    }

    public boolean canSharePeripheral() {
        return canSharePeripheral;
    }
}
