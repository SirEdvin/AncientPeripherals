package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.recipes.AutomataRecipe;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.PPAbilities;

import java.util.Map;
import java.util.Optional;

public class ScientificAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {

    public static final String TYPE = "scientificAutomataCore";

    public ScientificAutomataCorePeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(TYPE, turtle, side, AutomataCoreTier.TIER3);
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
          Map<String, Object> data = super.getPeripheralConfiguration();
          data.put("abstractiumXPPointsCost", ProgressivePeripheralsConfig.abstractiumXPPointsCost);
          return data;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult crystallizeXP(int ingots_count) throws LuaException {
        ExperienceAbility ability = owner.getAbility(PPAbilities.EXPERIENCE);
        if (ability == null)
            throw new LuaException("Unsupporterd operation");
        ItemStack result = new ItemStack(Items.ABSTRACTIUM_INGOT.get());
        if (ingots_count < result.getMaxStackSize())
            throw new LuaException("Count should less or equal stack count");
        if (ingots_count < 1)
            throw new LuaException("Count should be positive integer");
        int requiredXPAmount = ingots_count * ProgressivePeripheralsConfig.abstractiumXPPointsCost;
        CompoundNBT data = owner.getDataStorage();
        double currentAmount = ability._getStoredXP(data);
        if (currentAmount < requiredXPAmount)
            return MethodResult.of(null, String.format("Not enough xp stored: %.2f/%d", currentAmount, requiredXPAmount));
        int freeSlot = -1;
        IInventory turtleInventory = owner.turtle.getInventory();
        for (int slot = 0; slot < turtleInventory.getContainerSize(); slot++) {
            if (turtleInventory.getItem(slot).isEmpty()) {
                freeSlot = slot;
                break;
            }
        }
        if (freeSlot == -1)
            return MethodResult.of(null, "Cannot find free slot in turtle");
        ability.adjustStoredXP(-requiredXPAmount, data);
        result.setCount(ingots_count);
        turtleInventory.setItem(freeSlot, result);
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult craft() {
        IInventory inventory = owner.turtle.getInventory();
        Optional<AutomataRecipe> optRecipe = getWorld().getRecipeManager().getRecipeFor(AutomataRecipe.TYPE(), inventory, getWorld());
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find recipe");
        AutomataRecipe recipe = optRecipe.get();
        recipe.craft(inventory);
        return MethodResult.of(true);
    }
}
