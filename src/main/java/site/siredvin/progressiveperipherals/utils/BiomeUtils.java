package site.siredvin.progressiveperipherals.utils;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BiomeUtils {
    private static final Set<RegistryKey<Biome>> OVERWORLD_BIOMES = new HashSet<>();

    public static void setupOverworldBiomesSet() {
        // Get all vanilla and modded biomes with overworld tag
        OVERWORLD_BIOMES.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OVERWORLD));

        // Get all modded overworld biomes that doesn't have overworld tag
        OVERWORLD_BIOMES.addAll(BiomeManager.getAdditionalOverworldBiomes());
    }

    public static boolean isOverworldBiome(@NotNull BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null)
            return false;
        RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());
        return OVERWORLD_BIOMES.contains(biomeKey);
    }
}
