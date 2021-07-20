package site.siredvin.progressiveperipherals.common.setup;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.features.RealityBreakthroughPointFeature;

public class Features {
    public static final RegistryObject<Feature<NoFeatureConfig>> REALITY_BREAKTHROUGH_POINT = Registration.FEATURES.register(
            "reality_breakthrough_point", () -> new RealityBreakthroughPointFeature(NoFeatureConfig.CODEC)
    );

    public static void register() {
    }
}
