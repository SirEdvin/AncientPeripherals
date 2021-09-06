package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import de.srendi.advancedperipherals.lib.peripherals.owner.PeripheralOwnerAbility;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.ExperienceAbility;

public class PPAbilities {
    // TODO: refactor after AP release
    public static final PeripheralOwnerAbility<ExperienceAbility> EXPERIENCE;

    static {
        PeripheralOwnerAbility EXPERIENCE1;
        try {
            EXPERIENCE1 = PeripheralOwnerAbility.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            EXPERIENCE1 = null;
        }
        EXPERIENCE = EXPERIENCE1;
    }
}
