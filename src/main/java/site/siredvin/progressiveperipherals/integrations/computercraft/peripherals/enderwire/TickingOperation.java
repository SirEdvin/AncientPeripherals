package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public enum TickingOperation implements IPeripheralOperation<Object> {
    EXPOSE(60_000, 100, 10);

    private ForgeConfigSpec.IntValue cooldown;
    private ForgeConfigSpec.IntValue start_cost;
    private ForgeConfigSpec.IntValue tick_cost;
    private final int defaultCooldown;
    private final int defaultTickCost;
    private final int defaultStartCost;

    TickingOperation(int defaultCooldown, int defaultStartCost, int defaultTickCost) {
        this.defaultCooldown = defaultCooldown;
        this.defaultStartCost = defaultStartCost;
        this.defaultTickCost = defaultTickCost;
    }

    @Override
    public int getCooldown(Object o) {
        return cooldown.get();
    }

    @Override
    public int getCost(Object o) {
        return start_cost.get();
    }

    public int getTickCost(Object o) {
        return tick_cost.get();
    }

    @Override
    public Map<String, Object> computerDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", settingsName());
        data.put("type", getClass().getName());
        data.put("cooldown", cooldown.get());
        data.put("startCost", start_cost.get());
        data.put("tickCost", tick_cost.get());
        return data;
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        cooldown = builder.defineInRange(
                settingsName() + "Cooldown", defaultCooldown, 1_000, Integer.MAX_VALUE
        );
        start_cost = builder.defineInRange(
                settingsName() + "StartCost", defaultStartCost, 0, Integer.MAX_VALUE
        );
        tick_cost = builder.defineInRange(
                settingsName() + "TickCost", defaultTickCost, 0, Integer.MAX_VALUE
        );
    }
}
