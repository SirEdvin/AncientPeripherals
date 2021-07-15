package site.siredvin.progressiveperipherals.common.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ProgressivePeripheralsConfig {

    // Features
    public static boolean enableRealityForger;
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
    // Operations
    public static int transferXPCooldown;
    public static int transferXPCost;
    public static int enchantCooldown;
    public static int enchantCost;
    public static int smeltCooldown;
    public static int smithCooldown;
    public static int smithCost;
    // Configuration
    public static int scientificAutomataCoreInteractionRadius;
    public static int scientificAutomataCoreMaxFuelConsumptionLevel;
    public static int enchantingAutomataCoreInteractionRadius;
    public static int enchantingAutomataCoreMaxFuelConsumptionLevel;
    public static double enchantingAutomataCoreDisappearChance;
    public static int smithingAutomataCoreMaxFuelConsumptionLevel;

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

        // Operations
        final ForgeConfigSpec.IntValue TRANSFER_XP_COOLDOWN;
        final ForgeConfigSpec.IntValue TRANSFER_XP_COST;
        final ForgeConfigSpec.IntValue ENCHANT_XP_COOLDOWN;
        final ForgeConfigSpec.IntValue ENCHANT_XP_COST;
        final ForgeConfigSpec.IntValue SMELT_COOLDOWN;
        final ForgeConfigSpec.IntValue SMITH_COOLDOWN;
        final ForgeConfigSpec.IntValue SMITH_COST;

        // Mechanic souls
        final ForgeConfigSpec.IntValue SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS;
        final ForgeConfigSpec.IntValue SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL;
        final ForgeConfigSpec.IntValue ENCHANTING_AUTOMATA_CORE_INTERACTION_RADIUS;
        final ForgeConfigSpec.IntValue ENCHANTING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL;
        final ForgeConfigSpec.DoubleValue ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE;
        final ForgeConfigSpec.IntValue SMITHING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL;

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
            builder.pop();
            builder.comment("").push("Operations");
            TRANSFER_XP_COOLDOWN = builder.defineInRange("transferXPCooldown", 1_000, 1, Integer.MAX_VALUE);
            TRANSFER_XP_COST = builder.defineInRange("transferXPCost", 1, 0, Integer.MAX_VALUE);
            ENCHANT_XP_COOLDOWN = builder.defineInRange("enchantXPCooldown", 5_000, 1, Integer.MAX_VALUE);
            ENCHANT_XP_COST = builder.defineInRange("enchantXPCost", 10, 0, Integer.MAX_VALUE);
            SMELT_COOLDOWN = builder.defineInRange("smeltCooldown", 1_000, 1, Integer.MAX_VALUE);
            SMITH_COOLDOWN = builder.defineInRange("smithCooldown", 1_000, 1, Integer.MAX_VALUE);
            SMITH_COST = builder.defineInRange("smithCost", 1, 0, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("").push("Automata cores");
            SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS = builder.defineInRange("scientificAutomataCoreInteractionRadius", 4, 1, 64);
            SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL = builder.defineInRange("scientificAutomataCoreInteractionRadius", 4, 1, 64);
            ENCHANTING_AUTOMATA_CORE_INTERACTION_RADIUS = builder.defineInRange("enchantingAutomataCoreInteractionRadius", 4, 1, 64);
            ENCHANTING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL = builder.defineInRange("enchantingAutomataCoreMaxFuelConsumptionLevel", 4, 1, 64);
            ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE = builder.defineInRange("enchantingAutomataCoreDisappearChance", 0.05, 0.1, 1);
            SMITHING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL = builder.defineInRange("smithingAutomataCoreMaxFuelConsumptionLevel", 4, 1, 64);
            builder.pop();
        }
    }
}