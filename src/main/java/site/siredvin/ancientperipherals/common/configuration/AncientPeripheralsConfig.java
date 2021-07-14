package site.siredvin.ancientperipherals.common.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientPeripheralsConfig {

    // Features
    public static boolean enableRealityForger;
    public static boolean enableEnchantingAutomataCore;
    // Restictions
    public static int realityForgerRadius;
    public static int abstractiumXPPointsCost;
    public static int enchantLevelCost;
    public static int xpToFuelRate;
    // Operations
    public static int transferXPCooldown;
    public static int transferXPCost;
    public static int enchantCooldown;
    public static int enchantCost;
    // Configuration
    public static int scientificAutomataCoreInteractionRadius;
    public static int scientificAutomataCoreMaxFuelConsumptionLevel;
    public static int enchantingAutomataCoreInteractionRadius;
    public static int enchantingAutomataCoreMaxFuelConsumptionLevel;

    public static class CommonConfig {

        // Features
        final ForgeConfigSpec.BooleanValue ENABLE_REALITY_FORGER;
        final ForgeConfigSpec.BooleanValue ENABLE_ENCHATING_AUTOMATA_CORE;

        // Restrictions

        final ForgeConfigSpec.IntValue REALITY_FORGER_RADIUS;
        final ForgeConfigSpec.IntValue ABSTRACTIUM_XP_POINTS_COST;
        final ForgeConfigSpec.IntValue ENCHANT_LEVEL_COST;
        final ForgeConfigSpec.IntValue XP_TO_FUEL_RATE;

        // Operations
        final ForgeConfigSpec.IntValue TRANSFER_XP_COOLDOWN;
        final ForgeConfigSpec.IntValue TRANSFER_XP_COST;
        final ForgeConfigSpec.IntValue ENCHANT_XP_COOLDOWN;
        final ForgeConfigSpec.IntValue ENCHANT_XP_COST;

        // Mechanic souls
        final ForgeConfigSpec.IntValue SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS;
        final ForgeConfigSpec.IntValue SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL;
        final ForgeConfigSpec.IntValue ENCHANTING_AUTOMATA_CORE_INTERACTION_RADIUS;
        final ForgeConfigSpec.IntValue ENCHANTING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL;

        CommonConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("").push("Features");
            ENABLE_REALITY_FORGER = builder.define("enableRealityForger", true);
            ENABLE_ENCHATING_AUTOMATA_CORE = builder.define("enableEnchantingAutomataCore", true);
            builder.pop();
            builder.comment("").push("Restrictions");
            REALITY_FORGER_RADIUS = builder.defineInRange("realityForgerRadius", 8, 1, 64);
            ABSTRACTIUM_XP_POINTS_COST = builder.defineInRange("abstractiumXPPointsCost", 20, 5, Integer.MAX_VALUE);
            XP_TO_FUEL_RATE = builder.defineInRange("xpToFuelRate", 10, 1, Integer.MAX_VALUE);
            ENCHANT_LEVEL_COST = builder.defineInRange("enchantLevelCost", 30, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("").push("Operations");
            TRANSFER_XP_COOLDOWN = builder.defineInRange("transferXPCooldown", 1_000, 1, Integer.MAX_VALUE);
            TRANSFER_XP_COST = builder.defineInRange("transferXPCost", 1, 1, Integer.MAX_VALUE);
            ENCHANT_XP_COOLDOWN = builder.defineInRange("enchantXPCooldown", 5_000, 1, Integer.MAX_VALUE);
            ENCHANT_XP_COST = builder.defineInRange("enchantXPCost", 10, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("").push("Automata cores");
            SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS = builder.defineInRange("scientificAutomataCoreInteractionRadius", 4, 1, 64);
            SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL = builder.defineInRange("scientificAutomataCoreInteractionRadius", 4, 1, 64);
            ENCHANTING_AUTOMATA_CORE_INTERACTION_RADIUS = builder.defineInRange("enchantingAutomataCoreInteractionRadius", 4, 1, 64);
            ENCHANTING_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL = builder.defineInRange("enchantingAutomataCoreMaxFuelConsumptionLevel", 4, 1, 64);
            builder.pop();
        }
    }
}