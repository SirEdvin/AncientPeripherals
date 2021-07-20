package site.siredvin.progressiveperipherals.common.setup;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

public class ConfiguredFeatures {
    public static ConfiguredFeature<NoFeatureConfig, ?> REALITY_BREAKTHROUGH_POINT = Features.REALITY_BREAKTHROUGH_POINT
            .get().configured(NoFeatureConfig.INSTANCE);

    public static void register() {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;
        Registry.register(registry, new ResourceLocation(ProgressivePeripherals.MOD_ID, "reality_breakthrough_point"), REALITY_BREAKTHROUGH_POINT);
    }
}
