package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.ModelTransformingTurtle;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.FluidyAutomataCorePeripheral;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class FluidyTurtleUpgrade extends ModelTransformingTurtle<FluidyAutomataCorePeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, "fluidy_automata");

    public static class FluidCapability extends FluidTank {

        private final static String FLUID_TANK_TAG = "fluidTank";

        private final ITurtleAccess turtle;
        private final TurtleSide side;

        public FluidCapability(ITurtleAccess turtle, TurtleSide side) {
            super(16000);
            this.turtle = turtle;
            this.side = side;
            CompoundNBT data = turtle.getUpgradeNBTData(side).getCompound(FLUID_TANK_TAG);
            if (!data.isEmpty())
                this.readFromNBT(data);
        }

        @Override
        protected void onContentsChanged() {
            turtle.getUpgradeNBTData(side).put(FLUID_TANK_TAG, this.writeToNBT(new CompoundNBT()));
        }
    }

    public FluidyTurtleUpgrade() {
        super(ID, TranslationUtil.turtle("fluidy"), new ItemStack(Items.FLUIDY_AUTOMATA_CORE.get()));
    }

    @Override
    protected ModelResourceLocation getLeftModel() {
        return null;
    }

    @Override
    protected ModelResourceLocation getRightModel() {
        return null;
    }

    @Override
    protected FluidyAutomataCorePeripheral buildPeripheral(@NotNull ITurtleAccess iTurtleAccess, @NotNull TurtleSide turtleSide) {
        return new FluidyAutomataCorePeripheral("fluidyAutomataCore", iTurtleAccess, turtleSide);
    }

    @NotNull
    @Override
    public <T1> LazyOptional<T1> getCapability(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Capability<T1> capability) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> new FluidCapability(turtle, side)).cast();
        return super.getCapability(turtle, side, capability);
    }

    @Override
    public void invalidateCaps(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        super.invalidateCaps(turtle, side);
    }
}
