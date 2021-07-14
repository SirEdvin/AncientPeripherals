package site.siredvin.ancientperipherals.computercraft.peripherals;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.AutomataCorePeripheral;

public class SmithingAutomataCorePeripheral extends AutomataCorePeripheral {
    public SmithingAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    public int getInteractionRadius() {
        return 0;
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return 0;
    }

    @Override
    protected int getRawCooldown(String s) {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
