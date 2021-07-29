package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IAutomataCoreTier;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionBrewing;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.List;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.BREW;
import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.THROW_POTION;

public class BrewingAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {

    public BrewingAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    public IAutomataCoreTier getTier() {
        return AutomataCoreTier.TIER3;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        List<IPeripheralOperation<?>> operations = super.possibleOperations();
        operations.add(BREW);
        operations.add(THROW_POTION);
        return operations;
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableBrewingAutomataCore;
    }

    @LuaFunction
    public final MethodResult brew() {
        return withOperation(BREW, context -> {
            IInventory turtleInventory = turtle.getInventory();
            int selectedSlot = turtle.getSelectedSlot();
            ItemStack component = turtleInventory.getItem(selectedSlot);

            if (!PotionBrewing.isIngredient(component))
                return MethodResult.of(null, "Selected component is not an ingredient for brewing!");

            boolean usedForBrewing = false;

            for (int slot = 0; slot < turtleInventory.getContainerSize(); slot++) {
                if (slot == selectedSlot)
                    continue;

                ItemStack slotStack = turtleInventory.getItem(slot);
                if (slotStack.isEmpty())
                    continue;

                if (PotionBrewing.hasMix(slotStack, component)) {
                    turtleInventory.setItem(slot, PotionBrewing.mix(component, slotStack));
                    usedForBrewing = true;
                    adjustStoredXP(ProgressivePeripheralsConfig.brewingXPReward);
                }
            }
            if (usedForBrewing)
                if (component.getCount() == 1) {
                    turtleInventory.setItem(selectedSlot, ItemStack.EMPTY);
                } else {
                    component.setCount(component.getCount() - 1);
                }
            return MethodResult.of(usedForBrewing);
        });
    }

//    @LuaFunction
//    public final MethodResult throwPotion() {
//        return withOperation(BREW, context -> {
//            ItemStack selectedStack = turtle.getInventory().getItem(turtle.getSelectedSlot());
//            Potion potion = PotionUtils.getPotion(selectedStack);
//            // TODO: extraxt behavior from dispencer block, somehow ..
//            if (potion == Potions.EMPTY)
//                return MethodResult.of(null, "Selected item is not potion");
//            return MethodResult.of(true);
//        });
//    }
}
