package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.ModelTransformingTurtle;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.peripherals.TrainableAutomataCorePeripheral;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class TrainableTurtle extends ModelTransformingTurtle<TrainableAutomataCorePeripheral> {
    public TrainableTurtle() {
        super("trainable_automata", TranslationUtil.turtle("trainable"), new ItemStack(Items.TRAINABLE_AUTOMATA_CORE.get()));
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
    protected TrainableAutomataCorePeripheral buildPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new TrainableAutomataCorePeripheral("trainableAutomataCore", turtle, side);
    }
}
