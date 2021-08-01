package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireConnectorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;

public class EnderwireNetworkConnectorPeripheral extends BasePeripheral {
    private final EnderwireConnectorTileEntity tileEntity;
    public EnderwireNetworkConnectorPeripheral(String type, EnderwireConnectorTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction
    public final String getAttachedNetworkName() {
        return tileEntity.getAttachedNetwork();
    }

    @LuaFunction
    public final MethodResult inspectNetwork() {
        return withNetworks(getWorld(), data -> {
            String attachedNetwork = tileEntity.getAttachedNetwork();
            if (attachedNetwork == null)
                return MethodResult.of(null, "Not attached to any network");
            NetworkData network = data.getNetwork(attachedNetwork);
            if (network == null)
                return MethodResult.of(null, "Oh, this bad, missing network ...");
            return MethodResult.of(NetworkRepresentationTool.fullRepresentation(network, getPos()));
        });
    }
}
