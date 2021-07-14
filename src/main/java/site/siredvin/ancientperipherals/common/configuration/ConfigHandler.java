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
        AncientPeripheralsConfig.enableEnchantingAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_ENCHATING_AUTOMATA_CORE.get();
        // Restrictions
        AncientPeripheralsConfig.realityForgerRadius = ConfigHolder.COMMON_CONFIG.REALITY_FORGER_RADIUS.get();
        AncientPeripheralsConfig.abstractiumXPPointsCost = ConfigHolder.COMMON_CONFIG.ABSTRACTIUM_XP_POINTS_COST.get();
        AncientPeripheralsConfig.xpToFuelRate = ConfigHolder.COMMON_CONFIG.XP_TO_FUEL_RATE.get();
        AncientPeripheralsConfig.enchantLevelCost = ConfigHolder.COMMON_CONFIG.ENCHANT_LEVEL_COST.get();
        // Operations
        AncientPeripheralsConfig.transferXPCooldown = ConfigHolder.COMMON_CONFIG.TRANSFER_XP_COOLDOWN.get();
        AncientPeripheralsConfig.transferXPCost = ConfigHolder.COMMON_CONFIG.TRANSFER_XP_COST.get();
        AncientPeripheralsConfig.enchantCooldown = ConfigHolder.COMMON_CONFIG.ENCHANT_XP_COOLDOWN.get();
        AncientPeripheralsConfig.enchantCost = ConfigHolder.COMMON_CONFIG.ENCHANT_XP_COST.get();
        // Automata Cores
        AncientPeripheralsConfig.scientificAutomataCoreInteractionRadius = ConfigHolder.COMMON_CONFIG.SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS.get();
        AncientPeripheralsConfig.scientificAutomataCoreMaxFuelConsumptionLevel = ConfigHolder.COMMON_CONFIG.SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL.get();
        AncientPeripheralsConfig.enchantingAutomataCoreInteractionRadius = ConfigHolder.COMMON_CONFIG.ENCHANTING_AUTOMATA_CORE_INTERACTION_RADIUS.get();
        AncientPeripheralsConfig.enchantingAutomataCoreMaxFuelConsumptionLevel = ConfigHolder.COMMON_CONFIG.ENCHANTING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL.get();
    }
}
