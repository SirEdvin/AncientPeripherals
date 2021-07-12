package site.siredvin.ancientperipherals.common.configuration;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import site.siredvin.ancientperipherals.AncientPeripherals;

@Mod.EventBusSubscriber(modid = AncientPeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

    @SubscribeEvent
    public static void configEvent(final ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == ConfigHolder.COMMON_SPEC) {
            ConfigHandler.bakeCommon();
        }
    }

    @SubscribeEvent
    public static void reloadConfigEvent(final ModConfig.Reloading event) {
        if (event.getConfig().getSpec() == ConfigHolder.COMMON_SPEC) {
            ConfigHandler.bakeCommon();
        }
    }

    private static void bakeCommon() {
        // Features
        AncientPeripheralsConfig.enableRealityForger = ConfigHolder.COMMON_CONFIG.ENABLE_REALITY_FORGER.get();
        // Restrictions
        AncientPeripheralsConfig.realityForgerRadius = ConfigHolder.COMMON_CONFIG.REALITY_FORGER_RADIUS.get();
        AncientPeripheralsConfig.abstractiumXPPointsCost = ConfigHolder.COMMON_CONFIG.ABSTRACTIUM_XP_POINTS_COST.get();
        // Operations
        AncientPeripheralsConfig.collectXPCooldown = ConfigHolder.COMMON_CONFIG.COLLECT_XP_COOLDOWN.get();
        AncientPeripheralsConfig.collectXPCost = ConfigHolder.COMMON_CONFIG.COLLECT_XP_COST.get();
        // Automata Cores
        AncientPeripheralsConfig.trainableAutomataCoreInteractionRadius = ConfigHolder.COMMON_CONFIG.TRAINABLE_AUTOMATA_CORE_INTERACTION_RADIUS.get();
        AncientPeripheralsConfig.trainableAutomataCoreMaxFuelConsumptionLevel = ConfigHolder.COMMON_CONFIG.TRAINABLE_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL.get();
    }
}
