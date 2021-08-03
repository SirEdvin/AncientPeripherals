package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import net.minecraft.tileentity.ITickableTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireNetworkComponent;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireNetworkConnectorPeripheral;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwireConnectorTileEntity extends BaseEnderwireTileEntity<EnderwireConnectorTileEntity, EnderwireNetworkConnectorPeripheral> implements ITickableTileEntity {

    private long lastComputerEventMessage;

    public EnderwireConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_CONNECTOR.get());
        lastComputerEventMessage = -1;
    }

    @Override
    public void onAttachedNetworkChange() {
        if (this.attachedNetwork != null) {
            lastComputerEventMessage = EnderwireNetworkBusHub.getComputerEventsStart(attachedNetwork);
        }
    }

    @Override
    public EnderwireConnectorTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.CONNECTOR;
    }

    @Override
    public EnderwireNetworkComponent getComponentType() {
        return EnderwireNetworkComponent.CONNECTOR;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    @Override
    public void tick() {
        super.tick();
        if (attachedNetwork != null) {
            lastComputerEventMessage = EnderwireNetworkBusHub.traverseComputerEvents(attachedNetwork, lastComputerEventMessage, event -> {
                if (event.isValid(getBlockPos(), Objects.requireNonNull(getWorld()).dimension().location().toString())) {
                    getConnectedComputers().forEach(computer -> computer.queueEvent("enderwire_computer_event", event.getData()));
                } else {
                    getConnectedComputers().forEach(computer -> computer.queueEvent("malformed_enderwire_event"));
                }
            });
        }
    }

    @Override
    protected boolean hasPeripheral() {
        return true;
    }

    @Override
    protected @NotNull EnderwireNetworkConnectorPeripheral createPeripheral() {
        return new EnderwireNetworkConnectorPeripheral("enderwireNetworkConnector", this);
    }
}
