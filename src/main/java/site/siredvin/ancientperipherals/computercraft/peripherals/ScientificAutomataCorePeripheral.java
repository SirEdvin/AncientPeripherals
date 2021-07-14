package site.siredvin.ancientperipherals.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.recipes.AutomataRecipe;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.peripherals.abstractions.ExperienceAutomataCorePeripheral;

import java.util.Map;
import java.util.Optional;

public class ScientificAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {

    public ScientificAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return AncientPeripheralsConfig.scientificAutomataCoreMaxFuelConsumptionLevel;
    }

    @Override
    public int getInteractionRadius() {
        return AncientPeripheralsConfig.scientificAutomataCoreInteractionRadius;
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
          Map<String, Object> data = super.getPeripheralConfiguration();
          data.put("abstractiumXPPointsCost", AncientPeripheralsConfig.abstractiumXPPointsCost);
          return data;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction
    public final MethodResult crystallizeXP(int ingots_count) throws LuaException {
        ItemStack result = new ItemStack(Items.ABSTRACTIUM_INGOT.get());
        if (ingots_count < result.getMaxStackSize())
            throw new LuaException("Count should less or equal stack count");
        if (ingots_count < 1)
            throw new LuaException("Count should be positive integer");
        int requiredXPAmount = ingots_count * AncientPeripheralsConfig.abstractiumXPPointsCost;
        CompoundNBT data = owner.getDataStorage();
        int currentAmount = _getStoredXP(data);
        if (currentAmount < requiredXPAmount)
            return MethodResult.of(null, String.format("Not enough xp stored: %d/%d", currentAmount, requiredXPAmount));
        int freeSlot = -1;
        IInventory turtleInventory = turtle.getInventory();
        for (int slot = 0; slot < turtleInventory.getContainerSize(); slot++) {
            if (turtleInventory.getItem(slot).isEmpty()) {
                freeSlot = slot;
                break;
            }
        }
        if (freeSlot == -1)
            return MethodResult.of(null, "Cannot find free slot in turtle");
        adjustStoredXP(-requiredXPAmount, data);
        result.setCount(ingots_count);
        turtleInventory.setItem(freeSlot, result);
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
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
