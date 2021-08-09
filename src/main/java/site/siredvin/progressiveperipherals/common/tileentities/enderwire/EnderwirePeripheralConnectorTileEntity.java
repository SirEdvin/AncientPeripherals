package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.network.wired.IWiredNode;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static dan200.computercraft.shared.Capabilities.CAPABILITY_WIRED_ELEMENT;

public class EnderwirePeripheralConnectorTileEntity extends BaseEnderwireWiredTileEntity<EnderwirePeripheralConnectorTileEntity> implements ITickableTileEntity {
    private long lastNetworkEventMessage = -1;
    private boolean initialized = false;

    public EnderwirePeripheralConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_PERIPHERAL_CONNECTOR.get());
    }

    @Override
    public EnderwirePeripheralConnectorTileEntity getThis() {
        return this;
    }

    public void populateElement(EnderwireNetworkElement element) {
        Objects.requireNonNull(level);
        if (element.getElementType().isPopulateNetwork() && level.isLoaded(element.getPos()) && !element.getUUID().equals(getElementUUID())) {
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            Objects.requireNonNull(network);
            if (network.canReach(element, getPosition(), level.dimension().location().toString())) {
                TileEntity te = level.getBlockEntity(element.getPos());
                if (te != null)
                    te.getCapability(CAPABILITY_WIRED_ELEMENT).ifPresent(remoteWiredElement -> {
                        IWiredNode remoteNode = remoteWiredElement.getNode();
                        if (!remoteNode.equals(node))
                            node.connectTo(remoteNode);
                    });
            }
        }
    }

    public void depopulateElement(EnderwireNetworkElement element) {
        Objects.requireNonNull(level);
        if (element.getElementType().isPopulateNetwork() && level.isLoaded(element.getPos()) && !element.getUUID().equals(getElementUUID())) {
            TileEntity te = level.getBlockEntity(element.getPos());
            if (te != null)
                te.getCapability(CAPABILITY_WIRED_ELEMENT).ifPresent(remoteWiredElement -> {
                    IWiredNode remoteNode = remoteWiredElement.getNode();
                    if (!remoteNode.equals(node))
                        node.disconnectFrom(remoteNode);
                });
        }
    }

    @Override
    public void tick() {
        if (attachedNetwork != null && level != null && !level.isClientSide) {
            lastNetworkEventMessage = EnderwireNetworkBusHub.traverseNetworkEvents(attachedNetwork, lastNetworkEventMessage, event -> {
                Arrays.stream(event.getAddedElements()).forEach(this::populateElement);
                Arrays.stream(event.getRemovedElements()).forEach(this::depopulateElement);
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
                oldNetwork.traverseElements(this::depopulateElement);
            if (newNetworkName != null && newNetwork != null) {
                lastNetworkEventMessage = EnderwireNetworkBusHub.getNetworkEventsStart(attachedNetwork);
                newNetwork.traverseElements(this::populateElement);
            }
        }
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.NETWORK_CONNECTOR;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    @Override
    public void blockTick() {
        super.blockTick();
        String attachedNetwork = getAttachedNetwork();
        if (!initialized && level != null && !level.isClientSide && attachedNetwork != null) {
            initialized = true;
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            if (network != null) {
                network.traverseElements(this::populateElement);
            }
        }
    }
}
