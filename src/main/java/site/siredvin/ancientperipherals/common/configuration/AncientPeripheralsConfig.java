package site.siredvin.ancientperipherals.common.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientPeripheralsConfig {

    // Features
    public static boolean enableRealityForger;
    // Restictions
    public static int realityForgerRadius;
    public static int abstractiumXPPointsCost;
    // Operations
    public static int collectXPCooldown;
    public static int collectXPCost;
    // Configuration
    public static int scientificAutomataCoreInteractionRadius;
    public static int scientificAutomataCoreMaxFuelConsumptionLevel;

    public static class CommonConfig {

        // Features
        final ForgeConfigSpec.BooleanValue ENABLE_REALITY_FORGER;

        // Restrictions

        final ForgeConfigSpec.IntValue REALITY_FORGER_RADIUS;
        final ForgeConfigSpec.IntValue ABSTRACTIUM_XP_POINTS_COST;

        // Operations
        final ForgeConfigSpec.IntValue COLLECT_XP_COOLDOWN;
        final ForgeConfigSpec.IntValue COLLECT_XP_COST;

        // Mechanic souls
        final ForgeConfigSpec.IntValue SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS;
        final ForgeConfigSpec.IntValue SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL;

        CommonConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("").push("Features");
            ENABLE_REALITY_FORGER = builder.define("enableRealityForger", true);
            builder.pop();
            builder.comment("").push("Restrictions");
            REALITY_FORGER_RADIUS = builder.defineInRange("realityForgerRadius", 8, 1, 64);
            ABSTRACTIUM_XP_POINTS_COST = builder.defineInRange("abstractiumXPPointsCost", 20, 5, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("").push("Operations");
            COLLECT_XP_COOLDOWN = builder.defineInRange("collectXPCooldown", 1_000, 1, Integer.MAX_VALUE);
            COLLECT_XP_COST = builder.defineInRange("collectXPCost", 1, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("").push("Automata cores");
            SCIENTIFIC_AUTOMATA_CORE_INTERACTION_RADIUS = builder.defineInRange("scientificAutomataCoreInteractionRadius", 4, 1, 64);
            SCIENTIFIC_AUTOMATA_CORE_MAX_FUEL_CONSUMPTION_LEVEL = builder.defineInRange("scientificAutomataCoreInteractionRadius", 4, 1, 64);
            builder.pop();
        }
    }
}