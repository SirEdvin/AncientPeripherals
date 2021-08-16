package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.TurtleEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

import java.util.Objects;

public class EnderwireTurtleUpgradeModemPeripheral extends EnderwireUpgradeModemPeripheral {
    private final TurtleEnderwireElement elementProvider;

    public EnderwireTurtleUpgradeModemPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        super(turtle, side);
        elementProvider = new TurtleEnderwireElement(turtle, side);
    }

    @Override
    protected void attachToNewNetwork(@NotNull EnderwireNetwork newNetwork) {
        super.attachToNewNetwork(newNetwork);
        NetworkElementTool.connectToNewNetwork(elementProvider, newNetwork, (ServerWorld) getWorld());
        GlobalNetworksData data = GlobalNetworksData.get((ServerWorld) getWorld());
        IEnderwireNetworkElement element = Objects.requireNonNull(data.getNetwork(elementProvider.getAttachedNetwork())).getElement(elementProvider.getElementName());
        EnderwireNetworkBusHub.fireNetworkEvent(newNetwork.getName(), new EnderwireNetworkEvent.PeripheralAttached(element, elementProvider.getSharedPeripheral()));
    }

    @Override
    protected void removeFromOldNetwork(@NotNull EnderwireNetwork oldNetwork) {
        GlobalNetworksData data = GlobalNetworksData.get((ServerWorld) getWorld());
        IEnderwireNetworkElement element = Objects.requireNonNull(data.getNetwork(elementProvider.getAttachedNetwork())).getElement(elementProvider.getElementName());
        EnderwireNetworkBusHub.fireNetworkEvent(oldNetwork.getName(), new EnderwireNetworkEvent.PeripheralDetached(element, elementProvider.getSharedPeripheral()));
        super.removeFromOldNetwork(oldNetwork);
        NetworkElementTool.removeFromNetwork(oldNetwork.getName(), elementProvider, (ServerWorld) getWorld());
    }

    public TurtleEnderwireElement getEnderwireElement() {
        return elementProvider;
    }
}
