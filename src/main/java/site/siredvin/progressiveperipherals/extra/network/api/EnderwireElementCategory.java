package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementCategory {
    SENSOR(false, false, false, false),
    SERVICE(true, true, false, true),
    MODIFICATION(false, false, false, false),
    EMITTER(false, false, true, false),
    NONE(false, false, false, false);

    private final boolean receivingComputerEvents;
    private final boolean receivingNetworkEvents;
    private final boolean configurable;
    private final boolean populateNetwork;

    EnderwireElementCategory(boolean receivingComputerEvents, boolean receivingNetworkEvents, boolean configurable, boolean populateNetwork) {
        this.receivingComputerEvents = receivingComputerEvents;
        this.receivingNetworkEvents = receivingNetworkEvents;
        this.configurable = configurable;
        this.populateNetwork = populateNetwork;
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

    public boolean isPopulateNetwork() {
        return populateNetwork;
    }
}
