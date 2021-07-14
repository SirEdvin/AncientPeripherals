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
import site.siredvin.ancientperipherals.computercraft.peripherals.SmithingAutomataCorePeripheral;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class SmithingTurtle extends ModelTransformingTurtle<SmithingAutomataCorePeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "smithing_automata");
    public SmithingTurtle() {
        super(ID, TranslationUtil.turtle("smithing"), new ItemStack(Items.SMITHING_AUTOMATA_CORE.get()));
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
    protected SmithingAutomataCorePeripheral buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new SmithingAutomataCorePeripheral("smithingAutomataCore", turtle, side);
    }
}
