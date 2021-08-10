package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementCategory {
    SENSOR(),
    SERVICE(true),
    MODIFICATION(),
    EMITTER(),
    NONE();

    private final boolean populateNetwork;

    EnderwireElementCategory(boolean populateNetwork) {
        this.populateNetwork = populateNetwork;
    }

    EnderwireElementCategory() {
        this.populateNetwork = false;
    }

    public boolean isPopulateNetwork() {
        return populateNetwork;
    }
}
