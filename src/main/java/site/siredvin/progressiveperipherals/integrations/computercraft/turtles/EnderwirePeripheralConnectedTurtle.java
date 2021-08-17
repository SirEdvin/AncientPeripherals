package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.client.TransformedModel;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkAccessingTool;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireTurtleUpgradeModemPeripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.DataDependPeripheralTurtle;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class EnderwirePeripheralConnectedTurtle extends DataDependPeripheralTurtle<EnderwireTurtleUpgradeModemPeripheral> {

    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, "enderwire_peripheral_connected_turtle");
    public static final ModelResourceLocation NOT_CONNECTED_LEFT_MODEL = new ModelResourceLocation(new ResourceLocation(ProgressivePeripherals.MOD_ID, "turtle.enderwire.peripheral_connector_turtle_left"), "inventory");
    public static final ModelResourceLocation NOT_CONNECTED_RIGHT_MODEL = new ModelResourceLocation(new ResourceLocation(ProgressivePeripherals.MOD_ID, "turtle.enderwire.peripheral_connector_turtle_right"), "inventory");
    public static final ModelResourceLocation CONNECTED_LEFT_MODEL = new ModelResourceLocation(new ResourceLocation(ProgressivePeripherals.MOD_ID, "turtle.enderwire.peripheral_connector_turtle_left_connected"), "inventory");
    public static final ModelResourceLocation CONNECTED_RIGHT_MODEL = new ModelResourceLocation(new ResourceLocation(ProgressivePeripherals.MOD_ID, "turtle.enderwire.peripheral_connector_turtle_right_connected"), "inventory");

    public EnderwirePeripheralConnectedTurtle() {
        super(ID, TranslationUtil.turtle("enderwire_peripheral_connected"), new ItemStack(Blocks.ENDERWIRE_PERIPHERAL_CONNECTOR.get()));
    }

    @Override
    protected @NotNull TransformedModel getLeftModel(@Nullable ITurtleAccess turtle, @NotNull TurtleSide side) {
        if (turtle != null && NetworkAccessingTool.isSelectedNetworkPresent(turtle.getUpgradeNBTData(side)))
            return of(CONNECTED_LEFT_MODEL);
        return of(NOT_CONNECTED_LEFT_MODEL);
    }

    @Override
    protected @NotNull TransformedModel getRightModel(@Nullable ITurtleAccess turtle, @NotNull TurtleSide side) {
        if (turtle != null && NetworkAccessingTool.isSelectedNetworkPresent(turtle.getUpgradeNBTData(side)))
            return of(CONNECTED_RIGHT_MODEL);
        return of(NOT_CONNECTED_RIGHT_MODEL);
    }

    @Override
    protected EnderwireTurtleUpgradeModemPeripheral buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new EnderwireTurtleUpgradeModemPeripheral(turtle, side);
    }

    @Override
    public void update(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        IPeripheral peripheral = turtle.getPeripheral(side);
        if (peripheral instanceof EnderwireTurtleUpgradeModemPeripheral) {
            EnderwireTurtleUpgradeModemPeripheral turtlePeripheral = (EnderwireTurtleUpgradeModemPeripheral) peripheral;
            if (!turtlePeripheral.isInitialized())
                turtlePeripheral.initialize();
            if (turtlePeripheral.isExposed())
                turtlePeripheral.consumeTickFuel();
        }
    }
}
