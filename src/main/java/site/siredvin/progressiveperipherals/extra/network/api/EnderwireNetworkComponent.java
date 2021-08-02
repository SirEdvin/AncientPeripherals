package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireNetworkComponent {
    CONNECTOR,
    LEVER, BUTTON, PLATE,
    REDSTONE_EMITTER, LIGHT_EMITTER,
    UNKNOWN;

    public String getDisableEventName() {
        return name().toLowerCase() + "_disable";
    }

    public String getEnableEventName() {
        return name().toLowerCase() + "_enable";
    }

}
