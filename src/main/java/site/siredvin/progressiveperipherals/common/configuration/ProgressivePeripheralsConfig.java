package site.siredvin.progressiveperipherals.common.configuration;

import de.srendi.advancedperipherals.lib.misc.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTier;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.AutomataCoreTier;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.CountOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.TickingOperation;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProgressivePeripheralsConfig {

    private static final List<String> DEFAULT_REALITY_FORGER_BLACKLIST = new ArrayList<String>() {{
        add(Blocks.FLEXIBLE_STATUE.getId().toString());
        add(Blocks.FLEXIBLE_REALITY_ANCHOR.getId().toString());
        add(Blocks.REALITY_BREAKTHROUGH_POINT.getId().toString());
    }};

    private static final List<String> DEFAULT_RECIPE_REGISTRY_REFLECTION_BLACKLIST = new ArrayList<>();
    private static final List<String> DEFAULT_RECIPE_REGISTRY_RECIPE_TYPES_BLACKLIST = new ArrayList<String>() {{
        add("titanium:test_serializer"); // utility data
        add("immersiveengineering:blast_furnace_fuel"); // useless
        add("immersiveengineering:fertilizer"); // useless
        add("immersiveengineering:mineral_mix"); // useless
        add("create:conversion"); // utility data for jei
        add("industrialforegoing:stonework_generate"); // pretty useless, actually, still need manual machine configuration ...
        add("pneumaticcraft:fuel_quality"); // fuel data, not recipe
        add("pneumaticcraft:heat_properties"); // heat data, not recipe
        add("thermal:stirling_fuel");
        add("thermal:compression_fuel");
        add("thermal:magmatic_fuel");
        add("thermal:numismatic_fuel");
        add("thermal:lapidary_fuel");
        add("thermal:tree_extractor_boost");
        add("thermal:fisher_boost");
        add("thermal:potion_diffuser_boost");
        add("thermal:pulverizer_catalyst");
        add("thermal:smelter_catalyst");
        add("thermal:insolator_catalyst");
        add("tconstruct:part_builder");
        add("tconstruct:material");
        add("tconstruct:tinker_station");
        add("tconstruct:fuel");
        add("tconstruct:severing");
        add("tconstruct:data");
        add("mana-and-artifice:manaweaving-pattern-type");
        add("mana-and-artifice:runescribing-type");
        add("mana-and-artifice:runeforging-type");
    }};

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
    public static boolean enableItemRegistry;
    public static boolean enableEventDistributor;
    public static boolean enableScientificAutomataCore;
    public static boolean enableEnchantingAutomataCore;
    public static boolean enableSmithingAutomataCore;
    public static boolean enableBrewingAutomataCore;
    public static boolean enableCuttingAxe;
    public static boolean enableExtractingPickaxe;
    public static boolean enableCorrectingShovel;
    public static boolean enableIrrealiumTools;
    // Creative features
    public static boolean enableCreativeItemDuplicator;
    // Restictions
    public static int realityForgerRadius;
    public static int realityForgerMK2Radius;
    public static Set<? extends String> realityForgerBlacklist;
    public static int abstractiumXPPointsCost;
    public static int enchantLevelCost;
    public static int xpToFuelRate;
    public static double brewingXPReward;
    public static double furnaceBurnFuelCostRate;
    public static int cuttingAxeMaxBlockCount;
    public static int breakthroughPointSpawnChance;
    public static int recipeRegistryReflectionAllowedLevel;
    public static Set<? extends String> recipeRegistryReflectionBlacklist;
    public static Set<? extends String> recipeRegistryTypesBlacklist;
    // Machinery
    public static int extractorConsumeAmount;
    public static int extractorProduceAmount;
    public static boolean enableExtractor;
    public static boolean enableReactor;
    // Puzzles
    public static int linearSystemTimeLimit;
    // Automata cores
    public static double enchantingAutomataCoreDisappearChance;
    // Enderwire network
    public static boolean enderwireNetworkEnabled;
    public static int enderwireNetworkRangeStep;
    public static int enderwireNetworkComputerBusSize;
    public static int enderwireNetworkMaxElementCount;
    public static int enderwireNetworkMaxCountPerPlayer;
    // Debug and test features
    public static boolean strictPatchouli = false;

    public static class CommonConfig {

        // Features
        final ForgeConfigSpec.BooleanValue ENABLE_REALITY_FORGER;
        final ForgeConfigSpec.BooleanValue ENABLE_STATUE_WORKBENCH;
        final ForgeConfigSpec.BooleanValue ENABLE_ABSTRACTIUM_PEDESTAL;
        final ForgeConfigSpec.BooleanValue ENABLE_RECIPE_REGISTRY;
        final ForgeConfigSpec.BooleanValue ENABLE_ITEM_REGISTRY;
        final ForgeConfigSpec.BooleanValue ENABLE_EVENT_DISTRIBUTOR;
        final ForgeConfigSpec.BooleanValue ENABLE_ENCHATING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_SMITHING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_BREWING_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_SCIENTIFIC_AUTOMATA_CORE;
        final ForgeConfigSpec.BooleanValue ENABLE_CUTTING_AXE;
        final ForgeConfigSpec.BooleanValue ENABLE_EXTRACTING_PICKAXE;
        final ForgeConfigSpec.BooleanValue ENABLE_CORRECTING_SHOVEL;
        final ForgeConfigSpec.BooleanValue ENABLE_IRREALIUM_TOOLS;

        // Creative features

        final ForgeConfigSpec.BooleanValue ENABLE_CREATIVE_ITEM_DUPLICATOR;

        // Restrictions

        final ForgeConfigSpec.IntValue REALITY_FORGER_RADIUS;
        final ForgeConfigSpec.IntValue REALITY_FORGER_MK2_RADIUS;
        final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_FORGER_BLACKLIST;
        final ForgeConfigSpec.IntValue ABSTRACTIUM_XP_POINTS_COST;
        final ForgeConfigSpec.IntValue ENCHANT_LEVEL_COST;
        final ForgeConfigSpec.IntValue XP_TO_FUEL_RATE;
        final ForgeConfigSpec.DoubleValue BREWING_XP_REWARD;
        final ForgeConfigSpec.DoubleValue SMELT_FUEL_COST_RATE;
        final ForgeConfigSpec.IntValue CUTTING_AXE_MAX_BLOCK_COUNT;
        final ForgeConfigSpec.IntValue BREAKTHROUGH_POINT_SPAWN_CHANCE;
        final ForgeConfigSpec.IntValue RECIPE_REGISTRY_REFLECTION_ALLOWED_LEVEL;
        final ForgeConfigSpec.ConfigValue<List<? extends String>> RECIPE_REGISTRY_REFLECTION_BLACKLIST;
        final ForgeConfigSpec.ConfigValue<List<? extends String>> RECIPE_REGISTRY_TYPES_BLACKLIST;

        // Machinery

        final ForgeConfigSpec.IntValue EXTRACTOR_CONSUME_AMOUNT;
        final ForgeConfigSpec.IntValue EXTRACTOR_PRODUCE_AMOUNT;
        final ForgeConfigSpec.BooleanValue ENABLE_EXTRACTOR;
        final ForgeConfigSpec.BooleanValue ENABLE_REACTOR;

        // Puzzles

        final ForgeConfigSpec.IntValue LINEAR_SYSTEM_TIME_LIMIT;

        // Automata cores
        final ForgeConfigSpec.DoubleValue ENCHANTING_AUTOMATA_CORE_DISAPPEAR_CHANCE;

        // Enderwire

        final ForgeConfigSpec.BooleanValue ENDERWIRE_NETWORK_ENABLED;
        final ForgeConfigSpec.IntValue ENDERWIRE_NETWORK_RANGE_STEP;
        final ForgeConfigSpec.IntValue ENDERWIRE_NETWORK_COMPUTER_BUS_SIZE;
        final ForgeConfigSpec.IntValue ENDERWIRE_NETWORK_MAX_ELEMENT_COUNT;
        final ForgeConfigSpec.IntValue ENDERWIRE_NETWORK_MAX_COUNT_PER_PLAYER;


        CommonConfig(final ForgeConfigSpec.Builder builder) {
            builder.comment("").push("Features");
            ENABLE_REALITY_FORGER = builder.define("enableRealityForger", true);
            ENABLE_STATUE_WORKBENCH = builder.define("enableStatueWorkbench", true);
            ENABLE_ABSTRACTIUM_PEDESTAL = builder.define("enableAbstractiumPedestal", true);
            ENABLE_RECIPE_REGISTRY = builder.define("enableRecipeRegistry", true);
            ENABLE_ITEM_REGISTRY = builder.define("enableItemRegistry", true);
            ENABLE_EVENT_DISTRIBUTOR = builder.define("enableEventDistributor", true);
            ENABLE_ENCHATING_AUTOMATA_CORE = builder.define("enableEnchantingAutomataCore", true);
            ENABLE_SMITHING_AUTOMATA_CORE = builder.define("enableSmithingAutomataCore", true);
            ENABLE_BREWING_AUTOMATA_CORE = builder.define("enableBrewingAutomataCore", true);
            ENABLE_SCIENTIFIC_AUTOMATA_CORE = builder.comment("Defines is scientific automata core is enabled. Do not disable it without understanding of mod progression logic!").define("enableScientificAutomataCore", true);
            ENABLE_CUTTING_AXE = builder.define("enableCuttingAxe", true);
            ENABLE_EXTRACTING_PICKAXE = builder.define("enableExtractingPickaxe", true);
            ENABLE_CORRECTING_SHOVEL = builder.define("enableCorrectingShovel", true);
            ENABLE_IRREALIUM_TOOLS = builder.define("enableIrrealiumTools", true);
            builder.pop();

            builder.comment("").push("Creative features");
            ENABLE_CREATIVE_ITEM_DUPLICATOR = builder.define("enableCreativeItemDuplicator", true);
            builder.pop();

            builder.comment("").push("Restrictions");
            REALITY_FORGER_RADIUS = builder.defineInRange("realityForgerRadius", 8, 1, 64);
            REALITY_FORGER_MK2_RADIUS = builder.defineInRange("realityForgerMK2Radius", 64, 1, 256);
            REALITY_FORGER_BLACKLIST = builder.comment("Any block, that has tweak somehow it own model logic, should be here.").defineList(
                    "realityForgerBlacklist", DEFAULT_REALITY_FORGER_BLACKLIST, obj -> true
            );
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
            RECIPE_REGISTRY_REFLECTION_ALLOWED_LEVEL = builder
                    .comment("Define allowed recipe registry reflection level, 0 is disabled at all")
                    .defineInRange("recipeRegistryReflectionAllowedLevel", 2, 0, 8);
            RECIPE_REGISTRY_REFLECTION_BLACKLIST = builder
                    .comment("Define black listed method and fields for call")
                    .defineList("recipeRegistryReflectionBlacklist", DEFAULT_RECIPE_REGISTRY_REFLECTION_BLACKLIST, obj -> true);
            RECIPE_REGISTRY_TYPES_BLACKLIST = builder
                    .comment("Define black listed recipes types for recipe registry")
                    .defineList("recipeRegistryTypesBlacklist", DEFAULT_RECIPE_REGISTRY_RECIPE_TYPES_BLACKLIST, obj -> true);
            builder.pop();

            builder.comment("").push("Operations");
            register(CountOperation.values(), builder);
            register(SimpleOperation.values(), builder);
            register(FreeOperation.values(), builder);
            register(TickingOperation.values(), builder);
            register(TurtleDigOperationType.values(), builder);
            builder.pop();

            builder.comment("").push("Machinery");
            ENABLE_EXTRACTOR = builder.comment("Defines is reality breakthrough point extractor is enable. Do not disable it without understanding of mod progression logic!")
                    .define("enableExtractor", true);
            ENABLE_REACTOR = builder.comment("Defines is reality breakthrough point reactor is enable. Do not disable it without understanding of mod progression logic!")
                    .define("enableReactor", true);
            EXTRACTOR_CONSUME_AMOUNT = builder.comment("Defines amount of power that consumed from RBT point by one operation")
                    .defineInRange("extractorConsumeAmount", 1, 1, REASONABLE_POINT_POWER_LIMIT);
            EXTRACTOR_PRODUCE_AMOUNT = builder.comment("Defines amount of product that produced by extractor by one operation")
                    .defineInRange("extractorProduceAmount", 1, 1, REASONABLE_OPERATION_PRODUCE_LIMIT);
            register(RealityBreakthroughPointTier.values(), builder);
            builder.pop();

            builder.comment("").push("Puzzled");
            LINEAR_SYSTEM_TIME_LIMIT = builder.defineInRange("linearSystemTimeLimit", 2, REASONABLE_MIN_TIME_LIMIT, REASONABLE_MAX_TIME_LIMIT);
            builder.pop();

            builder.comment("").push("Enderwire");
            ENDERWIRE_NETWORK_ENABLED = builder.define("enderwireNetworkEnabled", true);
            ENDERWIRE_NETWORK_RANGE_STEP = builder.defineInRange("enderwireNetworkRangeStep", 64, 1, Integer.MAX_VALUE);
            ENDERWIRE_NETWORK_COMPUTER_BUS_SIZE = builder.defineInRange("enderwireNetworkComputerButSize", 512, 1, Integer.MAX_VALUE);
            ENDERWIRE_NETWORK_MAX_ELEMENT_COUNT = builder.defineInRange("enderwireNetworkMaxElementCount", 512, 1, Integer.MAX_VALUE);
            ENDERWIRE_NETWORK_MAX_COUNT_PER_PLAYER = builder.defineInRange("enderwireNetworkMaxCountPerPlayer", 16, 1, Integer.MAX_VALUE);
            register(EnderwireElementType.values(), builder);
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