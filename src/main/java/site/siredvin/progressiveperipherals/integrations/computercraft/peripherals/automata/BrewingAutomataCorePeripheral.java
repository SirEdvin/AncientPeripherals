package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.blocks.BlockTurtle;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IAutomataCoreTier;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.dispenser.*;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

import java.util.List;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.BREW;
import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.THROW_POTION;

public class BrewingAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {

    public static final String TYPE = "brewingAutomataCore";

    public BrewingAutomataCorePeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(TYPE, turtle, side);
    }

    private static class TurtlePotionDispenseBehavior implements IDispenseItemBehavior {

        private final double uncertaintyMultiply;
        private final double powerMultiply;

        TurtlePotionDispenseBehavior(double uncertaintyMultiply, double powerMultiply) {
            this.uncertaintyMultiply = uncertaintyMultiply;
            this.powerMultiply = powerMultiply;
        }

        public IPosition getDispensePosition(Direction direction, IBlockSource turtleBlockSource) {
            double x = turtleBlockSource.x() + 0.7 * direction.getStepX();
            double y = turtleBlockSource.y() + 0.7 * direction.getStepY();
            double z = turtleBlockSource.z() + 0.7 * direction.getStepZ();
            return new Position(x, y, z);
        }

        public final ItemStack dispense(IBlockSource turtleBlockSource, ItemStack stack) {
            ItemStack lvt_3_1_ = this.execute(turtleBlockSource, stack);
            this.playSound(turtleBlockSource);
            this.playAnimation(turtleBlockSource, turtleBlockSource.getBlockState().getValue(BlockTurtle.FACING));
            return lvt_3_1_;
        }

        public ItemStack execute(IBlockSource turtleBlockSource, ItemStack stack) {
            World world = turtleBlockSource.getLevel();
            Direction currentDirection = turtleBlockSource.getBlockState().getValue(BlockTurtle.FACING);
            IPosition targetPosition = getDispensePosition(currentDirection, turtleBlockSource);
            ProjectileEntity entity = this.getProjectile(world, targetPosition, stack);
            entity.shoot(currentDirection.getStepX(), ((float)currentDirection.getStepY() + 0.1F), currentDirection.getStepZ(), this.getPower(), this.getUncertainty());
            world.addFreshEntity(entity);
            stack.shrink(1);
            return stack;
        }

        protected void playSound(IBlockSource turtleBlockSource) {
            turtleBlockSource.getLevel().levelEvent(1002, turtleBlockSource.getPos(), 0);
        }

        protected void playAnimation(IBlockSource turtleBlockSource, Direction direction) {
            turtleBlockSource.getLevel().levelEvent(2000, turtleBlockSource.getPos(), direction.get3DDataValue());
        }

        protected float getUncertainty() {
            return (float) (6.0F * uncertaintyMultiply);
        }

        protected float getPower() {
            return (float) (1.1F * powerMultiply);
        }

        protected ProjectileEntity getProjectile(World world, IPosition targetPosition, ItemStack stack) {
            return Util.make(new PotionEntity(world, targetPosition.x(), targetPosition.y(), targetPosition.z()), (p_218413_1_) -> {
                p_218413_1_.setItem(stack);
            });
        }
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
                    component.shrink(1);
                }
            return MethodResult.of(usedForBrewing);
        });
    }

    @LuaFunction
    public final MethodResult throwPotion(@NotNull IArguments arguments) throws LuaException {
        double power = Math.min(arguments.optFiniteDouble(0, 1), 16);
        double uncertainty = Math.min(arguments.optFiniteDouble(1, 1), 16);
        if (power == 0)
            throw new LuaException("Power multiplicator cannot be 0");
        if (uncertainty == 0)
            throw new LuaException("Uncertainty multiplicator cannot be 0");
        return withOperation(THROW_POTION, context -> {
            int selectedSlot = turtle.getSelectedSlot();
            IInventory turtleInventory = turtle.getInventory();
            ItemStack selectedStack = turtleInventory.getItem(selectedSlot);
            Item selectedItem = selectedStack.getItem();
            if (selectedItem != Items.SPLASH_POTION && selectedItem != Items.LINGERING_POTION)
                return MethodResult.of(null, "Selected item should be splash or lingering potion");
            Potion potion = PotionUtils.getPotion(selectedStack);
            if (potion == Potions.EMPTY)
                return MethodResult.of(null, "Selected item is not potion");
            IDispenseItemBehavior behavior = new TurtlePotionDispenseBehavior(uncertainty, power);
            turtleInventory.setItem(selectedSlot, behavior.dispense(new ProxyBlockSource((ServerWorld) getWorld(), getPos()), selectedStack));
            return MethodResult.of(true);
        });
    }
}
