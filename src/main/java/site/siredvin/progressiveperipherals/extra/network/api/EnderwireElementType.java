package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementType {
    SENSOR(false, false),
    CONNECTOR(true, true);

    private final boolean receivingComputerEvents;
    private final boolean receivingNetworkEvents;

    EnderwireElementType(boolean receivingComputerEvents, boolean receivingNetworkEvents) {
        this.receivingComputerEvents = receivingComputerEvents;
        this.receivingNetworkEvents = receivingNetworkEvents;
    }

    public boolean isReceivingComputerEvents() {
        return receivingComputerEvents;
    }

    public boolean isReceivingNetworkEvents() {
        return receivingNetworkEvents;
    }
}
