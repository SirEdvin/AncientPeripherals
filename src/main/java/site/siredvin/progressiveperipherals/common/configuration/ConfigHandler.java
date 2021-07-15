package site.siredvin.progressiveperipherals.common.configuration;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

@Mod.EventBusSubscriber(modid = ProgressivePeripherals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
        ProgressivePeripheralsConfig.enableRealityForger = ConfigHolder.COMMON_CONFIG.ENABLE_REALITY_FORGER.get();
        ProgressivePeripheralsConfig.enableEnchantingAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_ENCHATING_AUTOMATA_CORE.get();
        ProgressivePeripheralsConfig.enableSmithingAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_SMITHING_AUTOMATA_CORE.get();
        ProgressivePeripheralsConfig.enableCuttingAxe = ConfigHolder.COMMON_CONFIG.ENABLE_CUTTING_AXE.get();
        ProgressivePeripheralsConfig.enableExtractingPickaxe = ConfigHolder.COMMON_CONFIG.ENABLE_EXTRACTING_PICKAXE.get();
        ProgressivePeripheralsConfig.enableCorrectingShovel = ConfigHolder.COMMON_CONFIG.ENABLE_CORRECTING_SHOVEL.get();
        // Restrictions
        ProgressivePeripheralsConfig.realityForgerRadius = ConfigHolder.COMMON_CONFIG.REALITY_FORGER_RADIUS.get();
        ProgressivePeripheralsConfig.abstractiumXPPointsCost = ConfigHolder.COMMON_CONFIG.ABSTRACTIUM_XP_POINTS_COST.get();
        ProgressivePeripheralsConfig.xpToFuelRate = ConfigHolder.COMMON_CONFIG.XP_TO_FUEL_RATE.get();
        ProgressivePeripheralsConfig.enchantLevelCost = ConfigHolder.COMMON_CONFIG.ENCHANT_LEVEL_COST.get();
        ProgressivePeripheralsConfig.furnaceBurnFuelCostRate = ConfigHolder.COMMON_CONFIG.SMELT_FUEL_COST_RATE.get();
        ProgressivePeripheralsConfig.cuttingAxeMaxBlockCount = ConfigHolder.COMMON_CONFIG.CUTTING_AXE_MAX_BLOCK_COUNT.get();
        // Operations
        ProgressivePeripheralsConfig.transferXPCooldown = ConfigHolder.COMMON_CONFIG.TRANSFER_XP_COOLDOWN.get();
        ProgressivePeripheralsConfig.transferXPCost = ConfigHolder.COMMON_CONFIG.TRANSFER_XP_COST.get();
        ProgressivePeripheralsConfig.enchantCooldown = ConfigHolder.COMMON_CONFIG.ENCHANT_XP_COOLDOWN.get();
        ProgressivePeripheralsConfig.enchantCost = ConfigHolder.COMMON_CONFIG.ENCHANT_XP_COST.get();
        ProgressivePeripheralsConfig.smeltCooldown = ConfigHolder.COMMON_CONFIG.SMELT_COOLDOWN.get();
        ProgressivePeripheralsConfig.smithCost = ConfigHolder.COMMON_CONFIG.SMITH_COST.get();
        ProgressivePeripheralsConfig.smithCooldown = ConfigHolder.COMMON_CONFIG.SMITH_COOLDOWN.get();
        // Automata Cores
        ProgressivePeripheralsConfig.scientificAutomataCoreInteractionRadius = ConfigHolder.COMMON_CONFIG.SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS.get();
        ProgressivePeripheralsConfig.scientificAutomataCoreMaxFuelConsumptionLevel = ConfigHolder.COMMON_CONFIG.SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL.get();
        ProgressivePeripheralsConfig.enchantingAutomataCoreInteractionRadius = ConfigHolder.COMMON_CONFIG.ENCHANTING_AUTOMATA_CORE_INTERACTION_RADIUS.get();
        ProgressivePeripheralsConfig.enchantingAutomataCoreMaxFuelConsumptionLevel = ConfigHolder.COMMON_CONFIG.ENCHANTING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL.get();
        ProgressivePeripheralsConfig.enchantingAutomataCoreDisappearChance = ConfigHolder.COMMON_CONFIG.ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE.get();
        ProgressivePeripheralsConfig.smithingAutomataCoreMaxFuelConsumptionLevel = ConfigHolder.COMMON_CONFIG.SMITHING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL.get();
    }
}
