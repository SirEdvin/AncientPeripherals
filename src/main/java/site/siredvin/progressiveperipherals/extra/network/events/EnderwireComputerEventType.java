package site.siredvin.progressiveperipherals.extra.network.events;

public enum EnderwireComputerEventType {
    LEVER_ENABLED, LEVER_DISABLED;

    public String computerName() {
        return this.name().toLowerCase();
    }
}
