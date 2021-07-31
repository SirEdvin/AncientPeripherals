package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import net.minecraft.tileentity.ITickableTileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.NetworkEventTool;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.network.EnderwireNetworkConnectorPeripheral;

public class EnderwireConnectorTileEntity extends BaseEnderwirePeripheralTileEntity<EnderwireConnectorTileEntity, EnderwireNetworkConnectorPeripheral> implements ITickableTileEntity {

    private long lastConsumedMessage;

    public EnderwireConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_CONNECTOR.get());
        lastConsumedMessage = -1;
    }

    @Override
    public void setAttachedNetwork(@Nullable String attachedNetwork) {
        super.setAttachedNetwork(attachedNetwork);
        if (this.attachedNetwork != null)
            lastConsumedMessage = NetworkEventTool.getComputerEventsStart(attachedNetwork);
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
    public void tick() {
        if (attachedNetwork != null) {
            lastConsumedMessage = NetworkEventTool.traverseComputerEvents(attachedNetwork, lastConsumedMessage, event -> {
                getConnectedComputers().forEach(computer -> computer.queueEvent(event.getName(), event.getData()));
            });
        }
    }

    @Override
    protected @NotNull EnderwireNetworkConnectorPeripheral createPeripheral() {
        return new EnderwireNetworkConnectorPeripheral("enderwireNetworkConnector", this);
    }
}
