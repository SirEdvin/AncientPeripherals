package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import net.minecraft.tileentity.ITickableTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireComputerEventType;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireNetworkConnectorPeripheral;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwireNetworkConnectorTileEntity extends BaseEnderwireTileEntity<EnderwireNetworkConnectorTileEntity, EnderwireNetworkConnectorPeripheral> implements ITickableTileEntity {

    private long lastComputerEventMessage = -1;

    public EnderwireNetworkConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_CONNECTOR.get());
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
    public EnderwireNetworkConnectorTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.SENSOR_MANAGER;
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
                    getConnectedComputers().forEach(computer -> computer.queueEvent(EnderwireComputerEventType.ENDERWIRE_COMPUTER_EVENT.name().toLowerCase(), event.getData()));
                } else {
                    getConnectedComputers().forEach(computer -> computer.queueEvent(EnderwireComputerEventType.MALFORMED_ENDERWIRE_EVENT.name().toLowerCase()));
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
        return new EnderwireNetworkConnectorPeripheral(this);
    }
}
