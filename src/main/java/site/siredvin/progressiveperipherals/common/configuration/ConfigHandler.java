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
        ProgressivePeripheralsConfig.enableStatueWorkbench = ConfigHolder.COMMON_CONFIG.ENABLE_STATUE_WORKBENCH.get();
        ProgressivePeripheralsConfig.enableAbstractiumPedestal = ConfigHolder.COMMON_CONFIG.ENABLE_ABSTRACTIUM_PEDESTAL.get();
        ProgressivePeripheralsConfig.enableEnchantingAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_ENCHATING_AUTOMATA_CORE.get();
        ProgressivePeripheralsConfig.enableSmithingAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_SMITHING_AUTOMATA_CORE.get();
        ProgressivePeripheralsConfig.enableScientificAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_SCIENTIFIC_AUTOMATA_CORE.get();
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
        ProgressivePeripheralsConfig.breakthroughPointSpawnChance = ConfigHolder.COMMON_CONFIG.BREAKTHROUGH_POINT_SPAWN_CHANCE.get();
        // Machinery
        ProgressivePeripheralsConfig.enableExtractor = ConfigHolder.COMMON_CONFIG.ENABLE_EXTRACTOR.get();
        ProgressivePeripheralsConfig.extractorConsumeAmount = ConfigHolder.COMMON_CONFIG.EXTRACTOR_CONSUME_AMOUNT.get();
        ProgressivePeripheralsConfig.extractorProduceAmount = ConfigHolder.COMMON_CONFIG.EXTRACTOR_PRODUCE_AMOUNT.get();
        // Puzzles
        ProgressivePeripheralsConfig.linearSystemTimeLimit = ConfigHolder.COMMON_CONFIG.LINEAR_SYSTEM_TIME_LIMIT.get();
        // Automata Cores
        ProgressivePeripheralsConfig.enchantingAutomataCoreDisappearChance = ConfigHolder.COMMON_CONFIG.ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE.get();
    }
}
