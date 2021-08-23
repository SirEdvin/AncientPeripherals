package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base;

import de.srendi.advancedperipherals.common.addons.computercraft.base.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public enum TurtleDigOperationType implements IConfigHandler {
    CORRECTING_SHOVEL(0),
    CUTTING_AXE(10),
    SILK_CUTTING_AXE(20),
    EXTRACTING_PICKAXE(1),
    FORTUNE_I_EXTRACTING_PICKAXE(2),
    FORTUNE_II_EXTRACTING_PICKAXE(3),
    FORTUNE_III_EXTRACTING_PICKAXE(4),
    SILK_EXTRACTING_PICKAXE(5);

    private ForgeConfigSpec.IntValue cost;
    private final int defaultCost;

    TurtleDigOperationType(int defaultCost) {
        this.defaultCost = defaultCost;
    }

    public int getCost() {
        return cost.get();
    }

    public Map<String, Object> computerDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", settingsName());
        data.put("type", getClass().getName());
        data.put("cost", cost.get());
        return data;
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        cost = builder.defineInRange(
                settingsName() + "Cost", defaultCost, 0, 64
        );
    }
}
