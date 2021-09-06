package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.lib.metaphysics.IAutomataCoreTier;
import de.srendi.advancedperipherals.lib.peripherals.AutomataCorePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.IPeripheralFunction;
import de.srendi.advancedperipherals.lib.peripherals.owner.OperationAbility;
import de.srendi.advancedperipherals.lib.peripherals.owner.PeripheralOwnerAbility;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.PPAbilities;

import java.util.Objects;

public abstract class ExperienceAutomataCorePeripheral extends AutomataCorePeripheral {

    public ExperienceAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side, IAutomataCoreTier tier) {
        super(type, turtle, side, tier);
        owner.attachAbility(PPAbilities.EXPERIENCE, new ExperienceAbility(this));
    }

    protected MethodResult withOperation(SimpleOperation operation, IPeripheralFunction<Object, MethodResult> function) throws LuaException {
        OperationAbility ability = owner.getAbility(PeripheralOwnerAbility.OPERATION);
        Objects.requireNonNull(ability);
        return ability.performOperation(operation, null, null, function, null);
    }
}
