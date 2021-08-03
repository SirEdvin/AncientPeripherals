package site.siredvin.progressiveperipherals.extra.network.api;

public enum EnderwireElementType {
    CONNECTOR(EnderwireElementCategory.CONNECTOR),
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

    public String getDisableEventName() {
        return name().toLowerCase() + "_disable";
    }

    public String getEnableEventName() {
        return name().toLowerCase() + "_enable";
    }

    public String getChangeEventName() {
        return name().toLowerCase() + "_change";
    }

}
