package site.siredvin.ancientperipherals.common.configuration;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHolder {
    public static final ForgeConfigSpec COMMON_SPEC;
    static final AncientPeripheralsConfig.CommonConfig COMMON_CONFIG;

    static {
        {
            final Pair<AncientPeripheralsConfig.CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AncientPeripheralsConfig.CommonConfig::new);
            COMMON_CONFIG = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }
}
