package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireEventSubscription;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;
import site.siredvin.progressiveperipherals.extra.network.events.IEnderwireEventConsumer;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkAccessingTool;

import java.util.Objects;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;

public class EnderwirePocketModemPeripheral extends BaseEnderwireModemPeripheral implements IEnderwireEventConsumer<EnderwireNetworkEvent> {

    private @Nullable EnderwireEventSubscription<EnderwireNetworkEvent> subscription;
    private boolean initialized = false;

    public EnderwirePocketModemPeripheral(@NotNull IPocketAccess pocket) {
        super(pocket);
    }

    protected void changeNetwork(GlobalNetworksData data, @Nullable EnderwireNetwork newNetwork) {
        CompoundNBT tag = owner.getDataStorage();
        EnderwireNetwork oldNetwork = NetworkAccessingTool.getSelectedNetwork(data, tag);
        NetworkAccessingTool.writeSelectedNetwork(tag, newNetwork);
        if (oldNetwork != null) {
            oldNetwork.traverseElements(this::detachPeripheral);
            if (subscription != null) {
                EnderwireNetworkBusHub.unsubscribeFromNetworkEvents(oldNetwork.getName(), subscription);
                subscription = null;
            }
        }
        if (newNetwork != null) {
            newNetwork.traverseElements(this::attachPeripheral);
            subscription = EnderwireNetworkBusHub.subscribeToNetworkEvents(newNetwork.getName(), this);
        }
    }

    public void attachPeripheral(GlobalNetworksData networks, @NotNull EnderwireNetworkElement element, @Nullable IPeripheral peripheral) {
        if (peripheral != null) {
            EnderwireNetwork network = NetworkAccessingTool.getSelectedNetwork(networks, owner.getDataStorage());
            Objects.requireNonNull(network);
            addSharedPeripheral(network.getName(), element, peripheral);
        }
    }

    public void attachPeripheral(@NotNull EnderwireNetworkElement element) {
        ServerWorld level = (ServerWorld) getWorld();
        if (element.getCategory().canSharePeripheral() && level.isLoaded(element.getPos())) {
            IEnderwireElement<?> enderwireElement = (IEnderwireElement<?>) level.getBlockEntity(element.getPos());
            if (enderwireElement != null)
                attachPeripheral(GlobalNetworksData.get(level), element, enderwireElement.getSharedPeripheral());
        }
    }

    public void detachPeripheral(@NotNull EnderwireNetworkElement element) {
        removeSharedPeripheral(element);
    }

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

    @LuaFunction(mainThread = true)
    public final MethodResult clearSelectedNetwork() throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            changeNetwork(data, null);
            return MethodResult.of(true);
        });
    }

    @Override
    public void consume(EnderwireNetworkEvent event) {
        ServerWorld world = (ServerWorld) getWorld();
        if (event instanceof EnderwireNetworkEvent.PeripheralAttached) {
            attachPeripheral(GlobalNetworksData.get(world), ((EnderwireNetworkEvent.PeripheralAttached) event).getElement(), ((EnderwireNetworkEvent.PeripheralAttached) event).getPeripheral());
        } else if (event instanceof EnderwireNetworkEvent.PeripheralDetached) {
            detachPeripheral(((EnderwireNetworkEvent.PeripheralDetached) event).getElement());
        }
    }

    @Override
    public void terminate() {
        ServerWorld world = (ServerWorld) getWorld();
        subscription = null; // to avoid subscription cleanup
        changeNetwork(GlobalNetworksData.get(world), null);
    }

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
                network.traverseElements(this::attachPeripheral);
                subscription = EnderwireNetworkBusHub.subscribeToNetworkEvents(network.getName(), this);
            } else {
                String networkName = NetworkAccessingTool.getSelectedNetworkName(tag);
                if (networkName != null)
                    NetworkAccessingTool.writeSelectedNetwork(tag, null);
            }
            initialized = true;
        }
    }
}
