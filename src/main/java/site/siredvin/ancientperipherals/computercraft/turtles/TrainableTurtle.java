package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BaseTurtle;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.peripherals.TrainableMechanicSoulPeripheral;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class TrainableTurtle extends BaseTurtle<TrainableMechanicSoulPeripheral> {
    public TrainableTurtle() {
        super("trainable_turtle", TranslationUtil.turtle("trainable"), new ItemStack(Items.TRAINABLE_MECHANIC_SOUL.get()));
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
    protected TrainableMechanicSoulPeripheral buildPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new TrainableMechanicSoulPeripheral("trainableMechaniSoul", turtle, side);
    }
}
