package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import de.srendi.advancedperipherals.lib.metaphysics.IAutomataCoreTier;
import net.minecraftforge.common.ForgeConfigSpec;


public enum AutomataCoreTier implements IAutomataCoreTier {
    TIER3(4, 4);

    private ForgeConfigSpec.IntValue interactionRadius;
    private ForgeConfigSpec.IntValue maxFuelConsumptionRate;
    private final int defaultInteractionRadius;
    private final int defaultMaxFuelConsumptionRate;

    @SuppressWarnings("SameParameterValue")
    AutomataCoreTier(int defaultInteractionRadius, int defaultMaxFuelConsumptionRate) {
        this.defaultInteractionRadius = defaultInteractionRadius;
        this.defaultMaxFuelConsumptionRate = defaultMaxFuelConsumptionRate;
    }

    @Override
    public int getInteractionRadius() {
        if (interactionRadius == null)
            return 0;
        return interactionRadius.get();
    }

    @Override
    public int getMaxFuelConsumptionRate() {
        if (maxFuelConsumptionRate == null)
            return 0;
        return maxFuelConsumptionRate.get();
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        interactionRadius = builder.defineInRange(
                settingsName() + "InteractionRadius", defaultInteractionRadius, 1, 64
        );
        maxFuelConsumptionRate = builder.defineInRange(
                settingsName() + "MaxFuelConsumptionRate", defaultMaxFuelConsumptionRate, 1, 32
        );
    }

    @Override
    public String settingsPostfix() {
        return "AutomataCore";
    }
}
