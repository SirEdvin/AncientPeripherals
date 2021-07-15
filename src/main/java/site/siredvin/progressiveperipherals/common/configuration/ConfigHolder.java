package site.siredvin.progressiveperipherals.common.configuration;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHolder {
    public static final ForgeConfigSpec COMMON_SPEC;
    static final ProgressivePeripheralsConfig.CommonConfig COMMON_CONFIG;

    static {
        {
            final Pair<ProgressivePeripheralsConfig.CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ProgressivePeripheralsConfig.CommonConfig::new);
            COMMON_CONFIG = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }
}
