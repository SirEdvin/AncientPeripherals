package site.siredvin.ancientperipherals.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.world.World;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.computercraft.peripherals.abstractions.ExperienceAutomataCorePeripheral;
import site.siredvin.ancientperipherals.utils.CheckUtils;
import site.siredvin.ancientperipherals.utils.LimitedInventory;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

public class SmithingAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {
    private final static int DEFAULT_SMELT_OPERATION_COST = 100;
    private final static String SMELT_OPERATION = "smelt";
    private final static String SMITH_OPERATION = "smith";

    public SmithingAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    public int getInteractionRadius() {
        return 0;
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return AncientPeripheralsConfig.smithingAutomataCoreMaxFuelConsumptionLevel;
    }

    @Override
    protected int getRawCooldown(String s) {
        if (s.equals(SMELT_OPERATION))
            return AncientPeripheralsConfig.smeltCooldown;
        if (s.equals(SMITH_OPERATION))
            return AncientPeripheralsConfig.smithCooldown;
        return 0;
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("smithCost", AncientPeripheralsConfig.smithCost);
        data.put("smithCooldown", AncientPeripheralsConfig.smithCooldown);
        data.put("smeltCooldown", AncientPeripheralsConfig.smeltCooldown);
        data.put("fuelCostRate", AncientPeripheralsConfig.furnaceBurnFuelCostRate);
        return data;
    }

    @Override
    public boolean isEnabled() {
        return AncientPeripheralsConfig.enableSmithingAutomataCore;
    }

    @LuaFunction
    public final MethodResult smith(int secondSlot, int targetSlot) throws LuaException {
        Optional<MethodResult> checkResults = cooldownCheck(SMITH_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        checkResults = consumeFuelOp(AncientPeripheralsConfig.smithCost);
        if (checkResults.isPresent()) return checkResults.get();
        CheckUtils.isCorrectSlot(secondSlot, "second");
        CheckUtils.isCorrectSlot(targetSlot);
        secondSlot--; // convert to Java slot
        targetSlot--; // convert to Java slot
        IInventory turtleInventory = turtle.getInventory();
        if (!turtleInventory.getItem(targetSlot).isEmpty())
            return MethodResult.of(null, "Target slot should be empty!");
        addRotationCycle();
        LimitedInventory limitedInventory = new LimitedInventory(turtleInventory, new int[]{turtle.getSelectedSlot(), secondSlot});
        World world = getWorld();
        Optional<SmithingRecipe> optRecipe = world.getRecipeManager().getRecipeFor(IRecipeType.SMITHING, limitedInventory, world);
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find smithing recipe");
        SmithingRecipe recipe = optRecipe.get();
        ItemStack result = recipe.assemble(limitedInventory);
        limitedInventory.reduceCount(0);
        limitedInventory.reduceCount(1);
        turtleInventory.setItem(targetSlot, result);
        trackOperation(SMITH_OPERATION);
        return MethodResult.of(true);
    }

    @LuaFunction
    public final MethodResult smelt(@Nonnull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResults = cooldownCheck(SMELT_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        IInventory turtleInventory = turtle.getInventory();
        LimitedInventory limitedInventory = new LimitedInventory(turtleInventory, new int[]{turtle.getSelectedSlot()});
        int targetSlot = arguments.getInt(0);
        CheckUtils.isCorrectSlot(targetSlot);
        targetSlot--;
        if (!turtleInventory.getItem(targetSlot).isEmpty())
            return MethodResult.of(null, "Target slot should be empty");
        int limit = arguments.optInt(1, Integer.MAX_VALUE);
        int smeltCount = Math.min(limit, limitedInventory.getItem(0).getCount());
        int requiredFuelCount = (int) (AncientPeripheralsConfig.furnaceBurnFuelCostRate * DEFAULT_SMELT_OPERATION_COST * smeltCount * _getFuelConsumptionRate());
        World world = getWorld();
        Optional<FurnaceRecipe> optRecipe = world.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, limitedInventory, world);
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find smelting recipe");
        checkResults = consumeFuelOp(requiredFuelCount);
        if (checkResults.isPresent()) return checkResults.get();
        addRotationCycle(smeltCount / 2 + 1);
        FurnaceRecipe recipe = optRecipe.get();
        ItemStack result = recipe.assemble(limitedInventory);
        result.setCount(smeltCount);
        turtleInventory.setItem(targetSlot, result);
        limitedInventory.reduceCount(0, smeltCount);
        adjustStoredXP((int) (smeltCount * recipe.getExperience()));
        trackOperation(SMELT_OPERATION, smeltCount);
        return MethodResult.of(true);
    }
}
