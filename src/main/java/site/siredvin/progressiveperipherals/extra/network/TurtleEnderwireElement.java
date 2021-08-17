package site.siredvin.progressiveperipherals.extra.network;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkAccessingTool;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireTurtlePeripheral;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TurtleEnderwireElement implements IEnderwireElement {
    private final @NotNull ITurtleAccess turtle;
    private final @NotNull TurtleSide side;
    private @Nullable String elementName;
    private boolean isPeripheralShared = false;
    private final @NotNull IPeripheral peripheral;

    public TurtleEnderwireElement(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        this.turtle = turtle;
        this.side = side;
        peripheral = new EnderwireTurtlePeripheral(turtle);
    }

    public CompoundNBT getDataStorage() {
        return turtle.getUpgradeNBTData(side);
    }

    @Override
    public BlockPos getPosition() {
        return turtle.getPosition();
    }

    @Override
    public @Nullable World getWorld() {
        return turtle.getWorld();
    }

    @Override
    public @Nullable String getAttachedNetwork() {
        return NetworkAccessingTool.getSelectedNetworkName(getDataStorage());
    }

    @Override
    public void setAttachedNetwork(@Nullable String name) {
        NetworkAccessingTool.writeSelectedNetwork(getDataStorage(), name, () -> turtle.updateUpgradeNBTData(side));
    }

    @Override
    public @Nullable String getElementName() {
        return elementName;
    }

    @Override
    public void setElementName(@Nullable String name) {
        elementName = name;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.TURTLE;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<String, Object>() {{
            put("fuelLevel", turtle.getFuelLevel());
            put("fuelLimit", turtle.getFuelLimit());
        }};
    }

    public void setPeripheralShared(boolean peripheralShared) {
        isPeripheralShared = peripheralShared;
    }

    public boolean isPeripheralShared() {
        return isPeripheralShared;
    }

    @Override
    public @Nullable IPeripheral getSharedPeripheral() {
        if (!isPeripheralShared)
            return null;
        return peripheral;
    }

    @Override
    public @NotNull IEnderwireNetworkElement generateElementData(@NotNull String elementName) {
        return new TurtleEnderwireNetworkElement(
                elementName, turtle, side, getElementType().getCategory(), getElementType(),
                Objects.requireNonNull(getWorld()).dimension().location().toString(), getAmplifier()
        );
    }
}
