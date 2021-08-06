package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import net.minecraft.tileentity.ITickableTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireNetworkConnectorPeripheral;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwireNetworkListenerTileEntity extends BaseEnderwireTileEntity<EnderwireNetworkListenerTileEntity, EnderwireNetworkConnectorPeripheral> implements ITickableTileEntity {

    private long lastComputerEventMessage = -1;

    public EnderwireNetworkListenerTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_LISTENER.get());
    }

    @Override
    public void onAttachedNetworkChange(String oldNetworkName, String newNetworkName) {
        if (level != null && !level.isClientSide) {
            if (newNetworkName != null) {
                lastComputerEventMessage = EnderwireNetworkBusHub.getComputerEventsStart(attachedNetwork);
            }
        }
    }

    @Override
    public EnderwireNetworkListenerTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.LISTENER;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    @Override
    public void tick() {
        if (attachedNetwork != null && level != null && !level.isClientSide) {
            lastComputerEventMessage = EnderwireNetworkBusHub.traverseComputerEvents(attachedNetwork, lastComputerEventMessage, event -> {
                if (event.IsNotMalformed(getBlockPos(), Objects.requireNonNull(getWorld()).dimension().location().toString())) {
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
