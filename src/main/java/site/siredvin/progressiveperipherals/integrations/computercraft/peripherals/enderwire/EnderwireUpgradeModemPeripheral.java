package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireComputerEventType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.events.*;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkAccessingTool;

import java.util.Objects;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;

public class EnderwireUpgradeModemPeripheral extends BaseEnderwireModemPeripheral implements IEnderwireEventConsumer<EnderwireNetworkEvent> {

    private @Nullable EnderwireEventSubscription<EnderwireNetworkEvent> networkSubscription;
    private @Nullable EnderwireEventSubscription<EnderwireComputerEvent> computerSubscription;
    private boolean initialized = false;

    public EnderwireUpgradeModemPeripheral(@NotNull IPocketAccess pocket) {
        super(pocket);
    }

    public EnderwireUpgradeModemPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        super(turtle, side);
    }

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        super.detach(computer);
        GlobalNetworksData data = GlobalNetworksData.get((ServerWorld) getWorld());
        EnderwireNetwork network = NetworkAccessingTool.getSelectedNetwork(data, owner.getDataStorage());
        if (network != null)
            removeFromOldNetwork(network);
    }

    protected void removeFromOldNetwork(@NotNull EnderwireNetwork oldNetwork) {
        oldNetwork.traverseElements(this::detachPeripheral);
        if (networkSubscription != null) {
            EnderwireNetworkBusHub.unsubscribeFromNetworkEvents(oldNetwork.getName(), networkSubscription);
            networkSubscription = null;
        }
        if (computerSubscription != null) {
            EnderwireNetworkBusHub.unsubscribeFromComputerEvents(oldNetwork.getName(), computerSubscription);
            computerSubscription = null;
        }
    }

    protected void attachToNewNetwork(@NotNull EnderwireNetwork newNetwork) {
        newNetwork.traverseElements(this::attachPeripheral);
        networkSubscription = EnderwireNetworkBusHub.subscribeToNetworkEvents(newNetwork.getName(), this);
        computerSubscription = EnderwireNetworkBusHub.subscribeToComputerEvents(newNetwork.getName(), this::consume);
    }

    protected void changeNetwork(GlobalNetworksData data, @Nullable EnderwireNetwork newNetwork) {
        CompoundNBT tag = owner.getDataStorage();
        EnderwireNetwork oldNetwork = NetworkAccessingTool.getSelectedNetwork(data, tag);
        NetworkAccessingTool.writeSelectedNetwork(tag, newNetwork);
        if (oldNetwork != null)
            removeFromOldNetwork(oldNetwork);
        if (newNetwork != null)
            attachToNewNetwork(newNetwork);
    }

    public void attachPeripheral(GlobalNetworksData networks, @NotNull IEnderwireNetworkElement element, @Nullable IPeripheral peripheral) {
        if (peripheral != null) {
            EnderwireNetwork network = NetworkAccessingTool.getSelectedNetwork(networks, owner.getDataStorage());
            Objects.requireNonNull(network);
            addSharedPeripheral(network.getName(), element, peripheral);
        }
    }

    public void attachPeripheral(@NotNull IEnderwireNetworkElement element) {
        ServerWorld level = (ServerWorld) getWorld();
        if (element.getCategory().canSharePeripheral() && level.isLoaded(element.getPos())) {
            IEnderwireElement enderwireElement = element.getElement(level);
            if (enderwireElement != null)
                attachPeripheral(GlobalNetworksData.get(level), element, enderwireElement.getSharedPeripheral());
        }
    }

    public void detachPeripheral(@NotNull IEnderwireNetworkElement element) {
        removeSharedPeripheral(element);
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult selectNetwork(@NotNull IArguments arguments) throws LuaException {
        String name = arguments.getString(0);
        String password = arguments.optString(1, null);
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            Pair<MethodResult, EnderwireNetwork> accessResult = NetworkAccessingTool.accessNetwork(data, name, player, password);
            if (accessResult.leftPresent())
                return accessResult.getLeft();
            EnderwireNetwork network = accessResult.getRight();
            if (!network.isInterdimensional())
                return MethodResult.of(null, "Only interdimensional network can be used!");
            changeNetwork(data, network);
            return MethodResult.of(true);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult clearSelectedNetwork() throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            changeNetwork(data, null);
            return MethodResult.of(true);
        });
    }

    @Override
    public void consume(EnderwireNetworkEvent event) {
        // Ignoring any check here because network should be all accessible
        ServerWorld world = (ServerWorld) getWorld();
        if (event instanceof EnderwireNetworkEvent.PeripheralAttached) {
            attachPeripheral(GlobalNetworksData.get(world), ((EnderwireNetworkEvent.PeripheralAttached) event).getElement(), ((EnderwireNetworkEvent.PeripheralAttached) event).getPeripheral());
        } else if (event instanceof EnderwireNetworkEvent.PeripheralDetached) {
            detachPeripheral(((EnderwireNetworkEvent.PeripheralDetached) event).getElement());
        }
    }

    public void consume(EnderwireComputerEvent event) {
        // Ignoring any check here because network should be all accessible
        getConnectedComputers().forEach(
                computer -> computer.queueEvent(EnderwireComputerEventType.ENDERWIRE_COMPUTER_EVENT.name().toLowerCase(),
                        event.getData())
        );
    }

    @Override
    public void terminate() {
        ServerWorld world = (ServerWorld) getWorld();
        // to avoid subscription cleanup
        networkSubscription = null;
        computerSubscription = null;
        changeNetwork(GlobalNetworksData.get(world), null);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInitialized() {
        return initialized;
    }

    public void initialize() {
        if (!initialized) {
            ServerWorld world = (ServerWorld) getWorld();
            GlobalNetworksData data = GlobalNetworksData.get(world);
            CompoundNBT tag = owner.getDataStorage();
            EnderwireNetwork network = NetworkAccessingTool.getSelectedNetwork(data, tag);
            if (network != null) {
                attachToNewNetwork(network);
            } else {
                String networkName = NetworkAccessingTool.getSelectedNetworkName(tag);
                if (networkName != null)
                    NetworkAccessingTool.writeSelectedNetwork(tag, (String) null);
            }
            initialized = true;
        }
    }
}
