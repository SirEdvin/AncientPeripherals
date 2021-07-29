package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IAutomataCoreTier;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.utils.CheckUtils;
import site.siredvin.progressiveperipherals.utils.LimitedInventory;

import java.util.List;
import java.util.Optional;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.SMITH;
import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.CountOperation.SMELT;

public class SmithingAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {

    public SmithingAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableSmithingAutomataCore;
    }

    @Override
    public IAutomataCoreTier getTier() {
        return AutomataCoreTier.TIER3;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        List<IPeripheralOperation<?>> data = super.possibleOperations();
        data.add(SMITH);
        data.add(SMELT);
        return data;
    }

    @LuaFunction
    public final MethodResult smith(int secondSlot, int targetSlot) throws LuaException {
        CheckUtils.isCorrectSlot(secondSlot, "second");
        CheckUtils.isCorrectSlot(targetSlot);
        int realTargetSlot = targetSlot - 1;
        int realSecondSlot = secondSlot - 1;
        return withOperation(SMITH, context -> {
            IInventory turtleInventory = turtle.getInventory();
            if (!turtleInventory.getItem(realTargetSlot).isEmpty())
                return MethodResult.of(null, "Target slot should be empty!");
            addRotationCycle();
            LimitedInventory limitedInventory = new LimitedInventory(turtleInventory, new int[]{turtle.getSelectedSlot(), realSecondSlot});
            World world = getWorld();
            Optional<SmithingRecipe> optRecipe = world.getRecipeManager().getRecipeFor(IRecipeType.SMITHING, limitedInventory, world);
            if (!optRecipe.isPresent())
                return MethodResult.of(null, "Cannot find smithing recipe");
            SmithingRecipe recipe = optRecipe.get();
            ItemStack result = recipe.assemble(limitedInventory);
            limitedInventory.reduceCount(0);
            limitedInventory.reduceCount(1);
            turtleInventory.setItem(realTargetSlot, result);
            return MethodResult.of(true);
        });
    }

    @LuaFunction
    public final MethodResult smelt(@NotNull IArguments arguments) throws LuaException {
        IInventory turtleInventory = turtle.getInventory();
        LimitedInventory limitedInventory = new LimitedInventory(turtleInventory, new int[]{turtle.getSelectedSlot()});
        int targetSlot = arguments.getInt(0);
        CheckUtils.isCorrectSlot(targetSlot);
        int realTargetSlot = targetSlot - 1;
        if (!turtleInventory.getItem(realTargetSlot).isEmpty())
            return MethodResult.of(null, "Target slot should be empty");
        int limit = arguments.optInt(1, Integer.MAX_VALUE);
        int smeltCount = Math.min(limit, limitedInventory.getItem(0).getCount());
        World world = getWorld();
        Optional<FurnaceRecipe> optRecipe = world.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, limitedInventory, world);
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find smelting recipe");
        return withOperation(SMELT, smeltCount, context -> {
            addRotationCycle(smeltCount / 2);
            FurnaceRecipe recipe = optRecipe.get();
            ItemStack result = recipe.assemble(limitedInventory);
            result.setCount(smeltCount);
            turtleInventory.setItem(realTargetSlot, result);
            limitedInventory.reduceCount(0, smeltCount);
            adjustStoredXP(smeltCount * recipe.getExperience());
            return MethodResult.of(true);
        }, null);
    }
}
