package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.lib.peripherals.owner.FuelAbility;
import de.srendi.advancedperipherals.lib.peripherals.owner.PeripheralOwnerAbility;
import de.srendi.advancedperipherals.lib.peripherals.owner.TurtleFuelAbility;
import de.srendi.advancedperipherals.lib.peripherals.owner.TurtlePeripheralOwner;
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

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.TickingOperation.EXPOSE;

public class EnderwireTurtleUpgradeModemPeripheral extends EnderwireUpgradeModemPeripheral {
    private final TurtleEnderwireElement elementProvider;
    private boolean isPeripheralSharingEnabled = false;

    public EnderwireTurtleUpgradeModemPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        super(turtle, side);
        elementProvider = new TurtleEnderwireElement(turtle, side);
        owner.attachOperation(EXPOSE);
        owner.attachAbility(PeripheralOwnerAbility.FUEL, new TurtleFuelAbility((TurtlePeripheralOwner) owner, 1));
    }

    protected void shareTurtlePeripheral() {
        if (isPeripheralSharingEnabled && !elementProvider.isPeripheralShared()) {
            GlobalNetworksData data = GlobalNetworksData.get((ServerWorld) getWorld());
            EnderwireNetwork network = data.getNetwork(elementProvider.getAttachedNetwork());
            Objects.requireNonNull(network);
            IEnderwireNetworkElement element = network.getElement(elementProvider.getElementName());
            Objects.requireNonNull(element);
            elementProvider.setPeripheralShared(true);
            EnderwireNetworkBusHub.fireNetworkEvent(network.getName(), new EnderwireNetworkEvent.PeripheralAttached(element, elementProvider.getTurtlePeripheral()));
        }
    }

    protected void hideTurtlePeripheral() {
        if (elementProvider.isPeripheralShared()) {
            GlobalNetworksData data = GlobalNetworksData.get((ServerWorld) getWorld());
            EnderwireNetwork network = data.getNetwork(elementProvider.getAttachedNetwork());
            Objects.requireNonNull(network);
            IEnderwireNetworkElement element = network.getElement(elementProvider.getElementName());
            Objects.requireNonNull(element);
            elementProvider.setPeripheralShared(false);
            EnderwireNetworkBusHub.fireNetworkEvent(network.getName(), new EnderwireNetworkEvent.PeripheralDetached(element, elementProvider.getTurtlePeripheral()));
        }
    }

    @Override
    protected void attachToNewNetwork(@NotNull EnderwireNetwork newNetwork) {
        super.attachToNewNetwork(newNetwork);
        NetworkElementTool.connectToNewNetwork(elementProvider, newNetwork, (ServerWorld) getWorld());
        shareTurtlePeripheral();
    }

    @Override
    protected void removeFromOldNetwork(@NotNull EnderwireNetwork oldNetwork) {
        hideTurtlePeripheral();
        super.removeFromOldNetwork(oldNetwork);
        NetworkElementTool.removeFromNetwork(oldNetwork.getName(), elementProvider, (ServerWorld) getWorld());
    }

    public TurtleEnderwireElement getEnderwireElement() {
        return elementProvider;
    }

    protected void stopExposing() {
        isPeripheralSharingEnabled = false;
        hideTurtlePeripheral();
    }

    public void consumeTickFuel() {
        if (isPeripheralSharingEnabled && elementProvider.isPeripheralShared()) {
            FuelAbility<?> fuelAbility = owner.getAbility(PeripheralOwnerAbility.FUEL);
            Objects.requireNonNull(fuelAbility);
            if (!fuelAbility.consumeFuel(EXPOSE.getTickCost(null), false))
                stopExposing();
        }
    }

    @LuaFunction
    public final boolean isExposed() {
        return isPeripheralSharingEnabled;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult expose() throws LuaException {
        if (elementProvider.getAttachedNetwork() == null)
            return MethodResult.of(null, "Turtle don't attached to any network");
        if (!isPeripheralSharingEnabled) {
            return withOperation(EXPOSE, null, null, ignored -> {
                isPeripheralSharingEnabled = true;
                shareTurtlePeripheral();
                return MethodResult.of(true);
            }, null);

        }
        return MethodResult.of(null, "Already exposed");
    }

    @LuaFunction(mainThread = true)
    public final MethodResult hide() {
        if (elementProvider.getAttachedNetwork() == null)
            return MethodResult.of(null, "Turtle don't attached to any network");
        if (isPeripheralSharingEnabled) {
            stopExposing();
            return MethodResult.of(true);
        }
        return MethodResult.of(null, "Already hidden");
    }
}
