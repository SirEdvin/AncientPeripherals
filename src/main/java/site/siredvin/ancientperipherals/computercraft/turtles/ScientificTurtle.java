package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.ModelTransformingTurtle;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.peripherals.ScientificAutomataCorePeripheral;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class ScientificTurtle extends ModelTransformingTurtle<ScientificAutomataCorePeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "scientific_automata");
    public ScientificTurtle() {
        super(ID, TranslationUtil.turtle("scientific"), new ItemStack(Items.SCIENTIFIC_AUTOMATA_CORE.get()));
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
    protected ScientificAutomataCorePeripheral buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new ScientificAutomataCorePeripheral("scientificAutomataCore", turtle, side);
    }
}
