package site.siredvin.ancientperipherals.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.MechanicSoulPeripheral;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import site.siredvin.ancientperipherals.common.recipes.AutomataRecipe;

import java.util.Optional;

public class TrainableMechanicSoulPeripheral extends MechanicSoulPeripheral {

    public TrainableMechanicSoulPeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return 1;
    }

    @Override
    protected int getRawCooldown(String s) {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction
    public final MethodResult craft() {
        IInventory inventory = turtle.getInventory();
        Optional<AutomataRecipe> optRecipe = getWorld().getRecipeManager().getRecipeFor(AutomataRecipe.TYPE, inventory, getWorld());
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find recipe");
        AutomataRecipe recipe = optRecipe.get();
        recipe.craft(inventory);
        return MethodResult.of(true);
    }
}
