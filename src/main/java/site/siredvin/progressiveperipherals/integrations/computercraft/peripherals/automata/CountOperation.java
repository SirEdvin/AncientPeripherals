package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum CountOperation implements IPeripheralOperation<Integer> {
    SMELT(1, 80);

    public enum CountPolicy {
        MULTIPLY(c -> c);

        private final Function<Integer, Integer> factorFunction;
        CountPolicy(Function<Integer, Integer> factorFunction) {
            this.factorFunction = factorFunction;
        }

        public int getFactor(int count) {
            return factorFunction.apply(count);
        }
    }

    private ForgeConfigSpec.IntValue cooldown;
    private ForgeConfigSpec.IntValue cost;
    private final int defaultCooldown;
    private final CountPolicy countCooldownPolicy;
    private final int defaultCost;
    private final CountPolicy countCostPolicy;

    @SuppressWarnings("SameParameterValue")
    CountOperation(
            int defaultCooldown, CountPolicy countCooldownPolicy, int defaultCost, CountPolicy countCostPolicy) {
        this.defaultCooldown = defaultCooldown;
        this.defaultCost = defaultCost;
        this.countCooldownPolicy = countCooldownPolicy;
        this.countCostPolicy = countCostPolicy;
    }

    @SuppressWarnings("SameParameterValue")
    CountOperation(int defaultCooldown, int defaultCost) {
        this(defaultCooldown, CountPolicy.MULTIPLY, defaultCost, CountPolicy.MULTIPLY);
    }

    @Override
    public int getCooldown(Integer context) {
        return cooldown.get() * countCooldownPolicy.getFactor(context);
    }

    @Override
    public int getCost(Integer context) {
        return cost.get() * countCostPolicy.getFactor(context);
    }

    @Override
    public Map<String, Object> computerDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", settingsName());
        data.put("type", getClass().getName());
        data.put("baseCooldown", cooldown.get());
        data.put("baseCost", cost.get());
        data.put("countCooldownPolicy", countCooldownPolicy.name().toLowerCase());
        data.put("countCostPolicy", countCostPolicy.name().toLowerCase());
        return data;
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        cooldown = builder.defineInRange(
                settingsName() + "Cooldown", defaultCooldown, 1_000, Integer.MAX_VALUE
        );
        cost = builder.defineInRange(
                settingsName() + "Cost", defaultCost, 0, Integer.MAX_VALUE
        );
    }
}
