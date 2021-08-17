package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireEventSubscription;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;
import site.siredvin.progressiveperipherals.extra.network.events.IEnderwireEventConsumer;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireModemPeripheral;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwirePeripheralConnectorTileEntity extends BaseEnderwireTileEntity<EnderwirePeripheralConnectorTileEntity, EnderwireModemPeripheral> implements IEnderwireEventConsumer<EnderwireNetworkEvent> {
    private boolean initialized = false;
    private EnderwireEventSubscription<EnderwireNetworkEvent> subscription;

    public EnderwirePeripheralConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_PERIPHERAL_CONNECTOR.get());
    }

    @Override
    public EnderwirePeripheralConnectorTileEntity getThis() {
        return this;
    }

    private void detachPeripherals() {
        if (level == null || !level.isClientSide) {
            if (peripheral != null)
                peripheral.clearSharedPeripherals();
        }
    }

    public void destroy() {
        detachPeripherals();
    }

    @Override
    public void placed() {
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        detachPeripherals();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        detachPeripherals();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void clearCache() {
        super.clearCache();
        SingleTickScheduler.now(this);
    }

    public void attachPeripheral(@NotNull IEnderwireNetworkElement element, @Nullable IPeripheral peripheral) {
        if (level != null && !level.isClientSide && peripheral != null && attachedNetwork != null) {
            ensurePeripheralCreated();
            Objects.requireNonNull(this.peripheral);
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            Objects.requireNonNull(network);
            if (network.canReach(element, getPosition(), level.dimension().location().toString()))
                this.peripheral.addSharedPeripheral(attachedNetwork, element, peripheral);
        }
    }

    public void attachPeripheral(@NotNull IEnderwireNetworkElement element) {
        Objects.requireNonNull(level);
        if (element.getCategory().canSharePeripheral() && level.isLoaded(element.getPos())) {
            IEnderwireElement enderwireElement = element.getElement(level);
            if (enderwireElement != null)
                attachPeripheral(element, enderwireElement.getSharedPeripheral());
        }
    }

    public void detachPeripheral(@NotNull IEnderwireNetworkElement element) {
        if (level != null && !level.isClientSide) {
            ensurePeripheralCreated();
            Objects.requireNonNull(this.peripheral);
            this.peripheral.removeSharedPeripheral(element);
        }
    }

    public void revalidatePeripheral(@NotNull IEnderwireNetworkElement element, @NotNull EnderwireNetwork network) {
        if (level != null && !level.isClientSide && element.getCategory().canSharePeripheral()) {
            ensurePeripheralCreated();
            Objects.requireNonNull(this.peripheral);
            if (network.canReach(element, getPosition(), level.dimension().location().toString())) {
                IEnderwireElement realWorldElement = element.getElement(getWorld());
                if (realWorldElement != null) {
                    IPeripheral peripheral = realWorldElement.getSharedPeripheral();
                    if (peripheral != null)
                        this.peripheral.addSharedPeripheral(network.getName(), element, peripheral);
                }
            } else {
                this.peripheral.removeSharedPeripheral(element);
            }
        }
    }

    @Override
    public void afterAttachedNetworkChange(String oldNetworkName, String newNetworkName) {
        if (level != null && !level.isClientSide) {
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork oldNetwork = networks.getNetwork(oldNetworkName);
            EnderwireNetwork newNetwork = networks.getNetwork(newNetworkName);
            if (oldNetwork != null) {
                oldNetwork.traverseElements(this::detachPeripheral);
                unsubscribeFromEvents(oldNetworkName);
            }
            if (newNetworkName != null && newNetwork != null) {
                subscribeToEvents(newNetworkName);
                newNetwork.traverseElements(this::attachPeripheral);
            }
        }
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.PERIPHERAL_CONNECTOR;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    @Override
    public void blockTick() {
        super.blockTick();
        if (!initialized && level != null && !level.isClientSide && attachedNetwork != null) {
            initialized = true;
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            if (network != null) {
                network.traverseElements(this::attachPeripheral);
                subscribeToEvents();
            }
        }
    }

    @Override
    protected boolean hasPeripheral() {
        return true;
    }

    @Override
    protected @NotNull EnderwireModemPeripheral createPeripheral() {
        return new EnderwireModemPeripheral(this);
    }

    @Override
    public void consume(EnderwireNetworkEvent event) {
        if (event instanceof EnderwireNetworkEvent.PeripheralAttached) {
            attachPeripheral(((EnderwireNetworkEvent.PeripheralAttached) event).getElement(), ((EnderwireNetworkEvent.PeripheralAttached) event).getPeripheral());
        } else if (event instanceof EnderwireNetworkEvent.PeripheralDetached) {
            detachPeripheral(((EnderwireNetworkEvent.PeripheralDetached) event).getElement());
        } else if (event instanceof EnderwireNetworkEvent.NetworkStatsChanged) {
            // In any case invalidation are very relevant
            EnderwireNetwork network = ((EnderwireNetworkEvent.NetworkStatsChanged) event).getNetwork();
            network.traverseElements(element -> revalidatePeripheral(element, network));
        }
    }

    public void subscribeToEvents(@NotNull String newNetwork) {
        if (level != null && !level.isClientSide && subscription == null)
            subscription = EnderwireNetworkBusHub.subscribeToNetworkEvents(newNetwork, this);
    }

    public void subscribeToEvents() {
        if (level != null && !level.isClientSide && attachedNetwork != null)
            subscribeToEvents(attachedNetwork);
    }

    public void unsubscribeFromEvents(@NotNull String oldNetwork) {
        if (level != null && !level.isClientSide && subscription != null) {
            EnderwireNetworkBusHub.unsubscribeFromNetworkEvents(oldNetwork, subscription);
            subscription = null;
        }
    }
}
