package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IAutomataCoreTier;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.AutomataCorePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.*;

public class FluidyAutomataCorePeripheral extends AutomataCorePeripheral {
    public FluidyAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    public IAutomataCoreTier getTier() {
        return AutomataCoreTier.TIER3;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.emptyList();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction
    public final MethodResult getFluidData() {
        return turtle.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(fluidHandler -> {
            FluidStack fluid = fluidHandler.getFluidInTank(0);
            if (fluid.isEmpty())
                return MethodResult.of(null, "No fluid stored");
            Map<String, Object> data = new HashMap<>();
            data.put("name", fluid.getDisplayName().getString());
            data.put("amount", fluid.getAmount());
            return MethodResult.of(data);
        }).orElse(MethodResult.of(null, "Cannot find tank"));
    }

    @LuaFunction
    public final MethodResult consumeFluid() {
        World world = getWorld();
        Objects.requireNonNull(world);
        return turtle.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(fluidHandler -> {
            BlockPos targetPos = getPos().relative(turtle.getDirection());
            FluidState state = world.getFluidState(targetPos).getFluidState();
            FluidStack stack = new FluidStack(state.getType(), state.getAmount());
            if (fluidHandler.fill(stack, IFluidHandler.FluidAction.SIMULATE) == 0)
                return MethodResult.of(null, "Cannot use this fluid");
            fluidHandler.fill(stack, IFluidHandler.FluidAction.EXECUTE);
            world.removeBlock(targetPos, true);
            return MethodResult.of(true);
        }).orElse(MethodResult.of(null, "Cannot find tank"));
    }
}
