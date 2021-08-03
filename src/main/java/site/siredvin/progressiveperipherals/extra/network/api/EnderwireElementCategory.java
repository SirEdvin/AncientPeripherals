package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementCategory {
    SENSOR(false, false, false),
    CONNECTOR(true, true, false),
    EMITTER(false, false, true),
    NONE(false, false, false);

    private final boolean receivingComputerEvents;
    private final boolean receivingNetworkEvents;
    private final boolean configurable;

    EnderwireElementCategory(boolean receivingComputerEvents, boolean receivingNetworkEvents, boolean configurable) {
        this.receivingComputerEvents = receivingComputerEvents;
        this.receivingNetworkEvents = receivingNetworkEvents;
        this.configurable = configurable;
    }

    public boolean isReceivingComputerEvents() {
        return receivingComputerEvents;
    }

    public boolean isReceivingNetworkEvents() {
        return receivingNetworkEvents;
    }

    public boolean isConfigurable() {
        return configurable;
    }
}
