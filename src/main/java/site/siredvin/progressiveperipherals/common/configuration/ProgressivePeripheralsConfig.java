package site.siredvin.progressiveperipherals.common.configuration;

import de.srendi.advancedperipherals.common.addons.computercraft.base.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTier;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.AutomataCoreTier;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.CountOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation;

public class ProgressivePeripheralsConfig {

    public static final int BREAKTHROUGH_SPAWN_CHANCE_LIMIT = 10_000;
    public static final int REASONABLE_POINT_POWER_LIMIT = 64;
    public static final int REASONABLE_OPERATION_PRODUCE_LIMIT = 64;

    public static final int REASONABLE_MIN_TIME_LIMIT = 1;
    public static final int REASONABLE_MAX_TIME_LIMIT = 120;

    // Features
    public static boolean enableRealityForger;
    public static boolean enableStatueWorkbench;
    public static boolean enableAbstractiumPedestal;
    public static boolean enableRecipeRegistry;
    public static boolean enableScientificAutomataCore;
    public static boolean enableEnchantingAutomataCore;
    public static boolean enableSmithingAutomataCore;
    public static boolean enableBrewingAutomataCore;
    public static boolean enableCuttingAxe;
    public static boolean enableExtractingPickaxe;
    public static boolean enableCorrectingShovel;
    // Restictions
    public static int realityForgerRadius;
    public static int realityForgerMK2Radius;
    public static int abstractiumXPPointsCost;
    public static int enchantLevelCost;
    public static int xpToFuelRate;
    public static double brewingXPReward;
    public static double furnaceBurnFuelCostRate;
    public static int cuttingAxeMaxBlockCount;
    public static int breakthroughPointSpawnChance;
    // Machinery
    public static int extractorConsumeAmount;
    public static int extractorProduceAmount;
    public static boolean enableExtractor;
    // Puzzles
    public static int linearSystemTimeLimit;
    // Configuration
    public static double enchantingAutomataCoreDisappearChance;

    public static class CommonConfig {

        // Features
        final ForgeConfigSpec.BooleanValue ENABLE_REALITY_FORGER;
        final ForgeConfigSpec.BooleanValue ENABLE_STATUE_WORKBENCH;
        final ForgeConfigSpec.BooleanValue ENABLE_ABSTRACTIUM_PEDESTAL;
        final ForgeConfigSpec.BooleanValue ENABLE_RECIPE_REGISTRY;
        final ForgeConfigSpec.BooleanValue ENABLE_ENCHATING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_SMITHING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_BREWING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_SCIENTIFIC_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_CUTTING_AXE;
        final ForgeConfigSpec.BooleanValue ENABLE_EXTRACTING_PICKAXE;
        final ForgeConfigSpec.BooleanValue ENABLE_CORRECTING_SHOVEL;

        // Restrictions

        final ForgeConfigSpec.IntValue REALITY_FORGER_RADIUS;
        final ForgeConfigSpec.IntValue REALITY_FORGER_MK2_RADIUS;
        final ForgeConfigSpec.IntValue ABSTRACTIUM_XP_POINTS_COST;
        final ForgeConfigSpec.IntValue ENCHANT_LEVEL_COST;
        final ForgeConfigSpec.IntValue XP_TO_FUEL_RATE;
        final ForgeConfigSpec.DoubleValue BREWING_XP_REWARD;
        final ForgeConfigSpec.DoubleValue SMELT_FUEL_COST_RATE;
        final ForgeConfigSpec.IntValue CUTTING_AXE_MAX_BLOCK_COUNT;
        final ForgeConfigSpec.IntValue BREAKTHROUGH_POINT_SPAWN_CHANCE;

        // Machinery

        final ForgeConfigSpec.IntValue EXTRACTOR_CONSUME_AMOUNT;
        final ForgeConfigSpec.IntValue EXTRACTOR_PRODUCE_AMOUNT;
        final ForgeConfigSpec.BooleanValue ENABLE_EXTRACTOR;

        // Puzzles

        final ForgeConfigSpec.IntValue LINEAR_SYSTEM_TIME_LIMIT;

        // Mechanic souls
        final ForgeConfigSpec.DoubleValue ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE;

        CommonConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("").push("Features");
            ENABLE_REALITY_FORGER = builder.define("enableRealityForger", true);
            ENABLE_STATUE_WORKBENCH = builder.define("enableStatueWorkbench", true);
            ENABLE_ABSTRACTIUM_PEDESTAL = builder.define("enableAbstractiumPedestal", true);
            ENABLE_RECIPE_REGISTRY = builder.define("enableRecipeRegistry", true);
            ENABLE_ENCHATING_AUTOMATA_CORE = builder.define("enableEnchantingAutomataCore", true);
            ENABLE_SMITHING_AUTOMATA_CORE = builder.define("enableSmithingAutomataCore", true);
            ENABLE_BREWING_AUTOMATA_CORE = builder.define("enableBrewingAutomataCore", true);
            ENABLE_SCIENTIFIC_AUTOMATA_CORE = builder.comment("Defines is scientific automata core is enabled. Do not disable it without understanding of mod progression logic!").define("enableScientificAutomataCore", true);
            ENABLE_CUTTING_AXE = builder.define("enableCuttingAxe", true);
            ENABLE_EXTRACTING_PICKAXE = builder.define("enableExtractingPickaxe", true);
            ENABLE_CORRECTING_SHOVEL = builder.define("enableCorrectingShovel", true);
            builder.pop();

            builder.comment("").push("Restrictions");
            REALITY_FORGER_RADIUS = builder.defineInRange("realityForgerRadius", 8, 1, 64);
            REALITY_FORGER_MK2_RADIUS = builder.defineInRange("realityForgerMK2Radius", 64, 1, 256);
            ABSTRACTIUM_XP_POINTS_COST = builder.defineInRange("abstractiumXPPointsCost", 20, 5, Integer.MAX_VALUE);
            XP_TO_FUEL_RATE = builder.defineInRange("xpToFuelRate", 10, 1, Integer.MAX_VALUE);
            BREWING_XP_REWARD = builder.defineInRange("brewingXPReward", 0.8, 0, 64);
            ENCHANT_LEVEL_COST = builder.defineInRange("enchantLevelCost", 30, 1, Integer.MAX_VALUE);
            SMELT_FUEL_COST_RATE = builder.defineInRange("smeltFuelCostRate", 0.8, 0.1, 1.5);
            CUTTING_AXE_MAX_BLOCK_COUNT = builder
                    .comment("Define max block count, that could be cut at once via Cutting axe. Don't set big numbers, it can cause server crashes")
                    .defineInRange("cuttingAxeMaxBlockCount", 1024, 256, Integer.MAX_VALUE);
            BREAKTHROUGH_POINT_SPAWN_CHANCE = builder
                    .comment("Spawn chance per 10000 calls")
                    .defineInRange("breakthroughPointSpawnChance", 20, 1, BREAKTHROUGH_SPAWN_CHANCE_LIMIT);

            builder.pop();

            builder.comment("").push("Operations");
            register(CountOperation.values(), builder);
            register(SimpleOperation.values(), builder);
            register(FreeOperation.values(), builder);
            builder.pop();

            builder.comment("").push("Machinery");
            ENABLE_EXTRACTOR = builder.comment("Defines is reality breakthrough point is enable. Do not disable it without understanding of mod progression logic!")
                    .define("enableExtractor", true);
            EXTRACTOR_CONSUME_AMOUNT = builder.comment("Defines amount of power that consumed from RBT point by one operation")
                    .defineInRange("extractorConsumeAmount", 1, 1, REASONABLE_POINT_POWER_LIMIT);
            EXTRACTOR_PRODUCE_AMOUNT = builder.comment("Defines amount of product that produced by extractor by one operation")
                    .defineInRange("extractorProduceAmount", 1, 1, REASONABLE_OPERATION_PRODUCE_LIMIT);
            register(RealityBreakthroughPointTier.values(), builder);
            builder.pop();

            builder.comment("").push("Puzzled");
            LINEAR_SYSTEM_TIME_LIMIT = builder.defineInRange("linearSystemTimeLimit", 2, REASONABLE_MIN_TIME_LIMIT, REASONABLE_MAX_TIME_LIMIT);
            builder.pop();

            builder.comment("").push("Automata cores");
            register(AutomataCoreTier.values(), builder);
            ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE = builder.defineInRange("enchantingAutomataCoreDisappearChance", 0.05, 0.1, 1);
            builder.pop();
        }

        protected void register(IConfigHandler[] data, final ForgeConfigSpec.Builder builder) {
            for (IConfigHandler handler: data) {
                handler.addToConfig(builder);
            }
        }
    }
}