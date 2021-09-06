package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base;

import de.srendi.advancedperipherals.lib.misc.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public enum TurtleDigOperationType implements IConfigHandler {
    CORRECTING_SHOVEL(0, 0, 0, 0),
    CUTTING_AXE(10, 0, 0, 10),
    EXTRACTING_PICKAXE(1, 0, 1, 4),
    SINGLE(0,0,0,0),
    VOIDING(1,0,0,0),
    ULTIMINE(1, 0.5, 1, 5),
    VOIDING_ULTIMINE(3, 0.5, 1, 5);

    private ForgeConfigSpec.DoubleValue baseCost;
    private ForgeConfigSpec.DoubleValue countMultiplier;
    private ForgeConfigSpec.DoubleValue fortuneMultiplier;
    private ForgeConfigSpec.DoubleValue silkMultiplier;
    private final double defaultBaseCost;
    private final double defaultCountMultiplier;
    private final double defaultFortuneMultiplier;
    private final double defaultSilkMultiplier;

    TurtleDigOperationType(double defaultBaseCost, double defaultCountMultiplier, double defaultFortuneMultiplier, double defaultSilkMultiplier) {
        this.defaultBaseCost = defaultBaseCost;
        this.defaultCountMultiplier = defaultCountMultiplier;
        this.defaultFortuneMultiplier = defaultFortuneMultiplier;
        this.defaultSilkMultiplier = defaultSilkMultiplier;
    }

    public int getCost(int count, int fortuneLevels, int silkLevels) {
        double finalCost = baseCost.get() + count * countMultiplier.get() + fortuneLevels * fortuneMultiplier.get() + silkLevels * silkMultiplier.get();
        return (int) Math.floor(finalCost);
    }

    public Map<String, Object> computerDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", settingsName());
        data.put("type", getClass().getName());
        data.put("cost", baseCost.get());
        data.put("countMultiplier", countMultiplier.get());
        data.put("fortuneMultiplier", fortuneMultiplier.get());
        data.put("silkMultiplier", silkMultiplier.get());
        return data;
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        baseCost = builder.defineInRange(settingsName() + "Cost", defaultBaseCost, 0, 64);
        countMultiplier = builder.defineInRange(settingsName() + "CountMultiplier", defaultCountMultiplier, 0, 64);
        fortuneMultiplier = builder.defineInRange(settingsName() + "fortuneMultiplier", defaultFortuneMultiplier, 0, 64);
        silkMultiplier = builder.defineInRange(settingsName() + "silkMultiplier", defaultSilkMultiplier, 0, 64);
    }
}
