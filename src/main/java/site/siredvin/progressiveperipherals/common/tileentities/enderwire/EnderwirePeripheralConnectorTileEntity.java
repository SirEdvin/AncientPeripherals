package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireModemPeripheral;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwirePeripheralConnectorTileEntity extends BaseEnderwireTileEntity<EnderwirePeripheralConnectorTileEntity, EnderwireModemPeripheral> implements ITickableTileEntity {
    private long lastNetworkEventMessage = -1;
    private boolean initialized = false;

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

    public void attachPeripheral(@NotNull EnderwireNetworkElement element, @Nullable IPeripheral peripheral) {
        if (peripheral != null && attachedNetwork != null) {
            ensurePeripheralCreated();
            Objects.requireNonNull(level);
            Objects.requireNonNull(this.peripheral);
            this.peripheral.addSharedPeripheral(attachedNetwork, element, peripheral);
        }
    }

    public void attachPeripheral(@NotNull EnderwireNetworkElement element) {
        Objects.requireNonNull(level);
        if (element.getCategory().canSharePeripheral() && level.isLoaded(element.getPos())) {
            ProgressivePeripherals.LOGGER.warn(String.format("Processing initial peripheral adding from %s", element.getName()));
            IEnderwireElement<?> enderwireElement = (IEnderwireElement<?>) level.getBlockEntity(element.getPos());
            if (enderwireElement != null)
                attachPeripheral(element, enderwireElement.getSharedPeripheral());
        }
    }

    public void detachPeripheral(@NotNull EnderwireNetworkElement element) {
        ensurePeripheralCreated();
        Objects.requireNonNull(level);
        Objects.requireNonNull(this.peripheral);
        this.peripheral.removeSharedPeripheral(element);
    }

    @Override
    public void tick() {
        if (attachedNetwork != null && level != null && !level.isClientSide) {
            lastNetworkEventMessage = EnderwireNetworkBusHub.traverseNetworkEvents(attachedNetwork, lastNetworkEventMessage, event -> {
                if (event instanceof EnderwireNetworkEvent.PeripheralAttached) {
                    ProgressivePeripherals.LOGGER.warn(String.format("Processing added event from %s", ((EnderwireNetworkEvent.PeripheralAttached) event).getElement().getName()));
                    attachPeripheral(((EnderwireNetworkEvent.PeripheralAttached) event).getElement(), ((EnderwireNetworkEvent.PeripheralAttached) event).getPeripheral());
                } else if (event instanceof EnderwireNetworkEvent.PeripheralDetached) {
                    ProgressivePeripherals.LOGGER.warn(String.format("Processing removed event from %s", ((EnderwireNetworkEvent.PeripheralDetached) event).getElement().getName()));
                    detachPeripheral(((EnderwireNetworkEvent.PeripheralDetached) event).getElement());
                }
            });
        }
    }

    @Override
    public void onAttachedNetworkChange(String oldNetworkName, String newNetworkName) {
        if (level != null && !level.isClientSide) {
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork oldNetwork = networks.getNetwork(oldNetworkName);
            EnderwireNetwork newNetwork = networks.getNetwork(newNetworkName);
            if (oldNetwork != null)
                oldNetwork.traverseElements(this::detachPeripheral);
            if (newNetworkName != null && newNetwork != null) {
                lastNetworkEventMessage = EnderwireNetworkBusHub.getNetworkEventsStart(attachedNetwork);
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
//            lastNetworkEventMessage = EnderwireNetworkBusHub.getNetworkEventsStart(attachedNetwork);
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            if (network != null) {
                network.traverseElements(this::attachPeripheral);
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
}
