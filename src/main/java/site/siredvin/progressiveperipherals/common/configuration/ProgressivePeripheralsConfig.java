package site.siredvin.progressiveperipherals.common.configuration;

import de.srendi.advancedperipherals.common.addons.computercraft.base.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.AutomataCoreTier;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.CountOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery.FreeMachineryOperation;

public class ProgressivePeripheralsConfig {

    public static final int BREAKTHROUGH_SPAWN_LIMIT = 10_000;

    // Features
    public static boolean enableRealityForger;
    public static boolean enableStatueWorkbench = true;
    public static boolean enableEnchantingAutomataCore;
    public static boolean enableSmithingAutomataCore;
    public static boolean enableCuttingAxe;
    public static boolean enableExtractingPickaxe;
    public static boolean enableCorrectingShovel;
    // Restictions
    public static int realityForgerRadius;
    public static int abstractiumXPPointsCost;
    public static int enchantLevelCost;
    public static int xpToFuelRate;
    public static double furnaceBurnFuelCostRate;
    public static int cuttingAxeMaxBlockCount;
    public static int breakthroughPointSpawnChance;
    // Configuration
    public static double enchantingAutomataCoreDisappearChance;

    public static class CommonConfig {

        // Features
        final ForgeConfigSpec.BooleanValue ENABLE_REALITY_FORGER;
        final ForgeConfigSpec.BooleanValue ENABLE_ENCHATING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_SMITHING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_CUTTING_AXE;
        final ForgeConfigSpec.BooleanValue ENABLE_EXTRACTING_PICKAXE;
        final ForgeConfigSpec.BooleanValue ENABLE_CORRECTING_SHOVEL;

        // Restrictions

        final ForgeConfigSpec.IntValue REALITY_FORGER_RADIUS;
        final ForgeConfigSpec.IntValue ABSTRACTIUM_XP_POINTS_COST;
        final ForgeConfigSpec.IntValue ENCHANT_LEVEL_COST;
        final ForgeConfigSpec.IntValue XP_TO_FUEL_RATE;
        final ForgeConfigSpec.DoubleValue SMELT_FUEL_COST_RATE;
        final ForgeConfigSpec.IntValue CUTTING_AXE_MAX_BLOCK_COUNT;
        final ForgeConfigSpec.IntValue BREAKTHROUGH_POINT_SPAWN_CHANCE;

        // Mechanic souls
        final ForgeConfigSpec.DoubleValue ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE;

        CommonConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("").push("Features");
            ENABLE_REALITY_FORGER = builder.define("enableRealityForger", true);
            ENABLE_ENCHATING_AUTOMATA_CORE = builder.define("enableEnchantingAutomataCore", true);
            ENABLE_SMITHING_AUTOMATA_CORE = builder.define("enableSmithingAutomataCore", true);
            ENABLE_CUTTING_AXE = builder.define("enableCuttingAxe", true);
            ENABLE_EXTRACTING_PICKAXE = builder.define("enableExtractingPickaxe", true);
            ENABLE_CORRECTING_SHOVEL = builder.define("enableCorrectingShovel", true);
            builder.pop();
            builder.comment("").push("Restrictions");
            REALITY_FORGER_RADIUS = builder.defineInRange("realityForgerRadius", 8, 1, 64);
            ABSTRACTIUM_XP_POINTS_COST = builder.defineInRange("abstractiumXPPointsCost", 20, 5, Integer.MAX_VALUE);
            XP_TO_FUEL_RATE = builder.defineInRange("xpToFuelRate", 10, 1, Integer.MAX_VALUE);
            ENCHANT_LEVEL_COST = builder.defineInRange("enchantLevelCost", 30, 1, Integer.MAX_VALUE);
            SMELT_FUEL_COST_RATE = builder.defineInRange("smeltFuelCostRate", 0.8, 0.1, 1.5);
            CUTTING_AXE_MAX_BLOCK_COUNT = builder
                    .comment("Define max block count, that could be cut at once via Cutting axe. Don't set big numbers, it can cause server crashes")
                    .defineInRange("cuttingAxeMaxBlockCount", 1024, 256, Integer.MAX_VALUE);
            BREAKTHROUGH_POINT_SPAWN_CHANCE = builder
                    .comment("Spawn chance per 10000 calls")
                    .defineInRange("breakthroughPointSpawnChance", 20, 1, BREAKTHROUGH_SPAWN_LIMIT);

            builder.pop();
            builder.comment("").push("Operations");
            register(CountOperation.values(), builder);
            register(SimpleOperation.values(), builder);
            register(FreeMachineryOperation.values(), builder);
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