package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementType {
    LISTENER(EnderwireElementCategory.SERVICE), PERIPHERAL_CONNECTOR(EnderwireElementCategory.SERVICE), NETWORK_CONNECTOR(EnderwireElementCategory.SERVICE),
    LEVER(EnderwireElementCategory.SENSOR), BUTTON(EnderwireElementCategory.SENSOR), PLATE(EnderwireElementCategory.SENSOR), REDSTONE_SENSOR(EnderwireElementCategory.SENSOR),
    REDSTONE_EMITTER(EnderwireElementCategory.EMITTER), LIGHT_EMITTER(EnderwireElementCategory.EMITTER),
    UNKNOWN(EnderwireElementCategory.NONE);

    private final EnderwireElementCategory category;

    EnderwireElementType(EnderwireElementCategory category) {
        this.category = category;
    }

    public EnderwireElementCategory getCategory() {
        return category;
    }

    public String getDisabledEventName() {
        return name().toLowerCase() + "_disabled";
    }

    public String getEnabledEventName() {
        return name().toLowerCase() + "_enabled";
    }

    public String getChangedEventName() {
        return name().toLowerCase() + "_changed";
    }

}
