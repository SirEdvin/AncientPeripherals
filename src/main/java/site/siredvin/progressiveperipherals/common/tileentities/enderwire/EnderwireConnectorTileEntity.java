package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import net.minecraft.tileentity.ITickableTileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.NetworkEventTool;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireNetworkConnectorPeripheral;

public class EnderwireConnectorTileEntity extends BaseEnderwireTileEntity<EnderwireConnectorTileEntity, EnderwireNetworkConnectorPeripheral> implements ITickableTileEntity {

    private long lastComputerEventMessage;

    public EnderwireConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_CONNECTOR.get());
        lastComputerEventMessage = -1;
    }

    @Override
    public void setAttachedNetwork(@Nullable String attachedNetwork) {
        super.setAttachedNetwork(attachedNetwork);
        if (this.attachedNetwork != null) {
            lastComputerEventMessage = NetworkEventTool.getComputerEventsStart(attachedNetwork);
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
    public String getDeviceType() {
        return "enderwireConnector";
    }

    @Override
    public void tick() {
        if (attachedNetwork != null) {
            lastComputerEventMessage = NetworkEventTool.traverseComputerEvents(attachedNetwork, lastComputerEventMessage, event -> {
                getConnectedComputers().forEach(computer -> computer.queueEvent("enderwire_computer_event", event.getName(), event.getData()));
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
