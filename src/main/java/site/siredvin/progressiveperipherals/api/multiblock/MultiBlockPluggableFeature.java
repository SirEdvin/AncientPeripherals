package site.siredvin.progressiveperipherals.api.multiblock;

import java.util.HashMap;
import java.util.Map;

public enum MultiBlockPluggableFeature {
    STORAGE_SMALL(
            MultiBlockPluggableFeatureType.STORAGE,
            new HashMap<String, Object>(){{ put(MultiBlockPluggableFeatureType.STORAGE_SIZE, 27); }}
    ),
    STORAGE_MIDDLE(
            MultiBlockPluggableFeatureType.STORAGE,
            new HashMap<String, Object>(){{ put(MultiBlockPluggableFeatureType.STORAGE_SIZE, 54); }}
    ),
    STORAGE_BIG(
            MultiBlockPluggableFeatureType.STORAGE,
            new HashMap<String, Object>(){{ put(MultiBlockPluggableFeatureType.STORAGE_SIZE, 108); }}
    );

    private final MultiBlockPluggableFeatureType type;
    private final Map<String, Object> configuration;

    MultiBlockPluggableFeature(MultiBlockPluggableFeatureType type, Map<String, Object> configuration) {
        this.type = type;
        this.configuration = configuration;
    }

    public MultiBlockPluggableFeatureType getType() {
        return type;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public int getIntegerValue(String name) {
        return ((Number)configuration.getOrDefault(name, 0)).intValue();
    }
}
