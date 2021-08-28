package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BaseTurtle;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.tools.HandIrrealiumToolPeripheral;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class TurtleIrrealiumHand extends BaseTurtle<HandIrrealiumToolPeripheral> {
    public static final String CORE_NAME = "irrealium_hand";
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, CORE_NAME);

    public TurtleIrrealiumHand() {
        super(ID, TranslationUtil.turtle(CORE_NAME), new ItemStack(Items.IRREALIUM_HAND.get()));
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
    protected HandIrrealiumToolPeripheral buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new HandIrrealiumToolPeripheral(turtle, side);
    }
}
