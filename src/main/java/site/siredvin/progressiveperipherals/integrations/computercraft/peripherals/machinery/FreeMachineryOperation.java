package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public enum FreeMachineryOperation implements IPeripheralOperation<Object> {
    EXTRACT(5_000);

    private ForgeConfigSpec.IntValue cooldown;
    private final int defaultCooldown;

    FreeMachineryOperation(int defaultCooldown) {
        this.defaultCooldown = defaultCooldown;
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        cooldown = builder.defineInRange(
                settingsName() + "Cooldown", defaultCooldown, 1_000, Integer.MAX_VALUE
        );
    }

    @Override
    public int getCooldown(Object context) {
        return cooldown.get();
    }

    @Override
    public int getCost(Object context) {
        return 0;
    }

    @Override
    public Map<String, Object> computerDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", settingsName());
        data.put("type", getClass().getSimpleName());
        data.put("cooldown", cooldown.get());
        return data;
    }
}
