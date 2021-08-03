package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireConnectorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @LuaFunction(mainThread = true)
    public final MethodResult getNetworkElementStates() {
        World world = getWorld();
        return withNetworks(getWorld(), data -> {
            String attachedNetwork = tileEntity.getAttachedNetwork();
            if (attachedNetwork == null)
                return MethodResult.of(null, "Not attached to any network");
            NetworkData network = data.getNetwork(attachedNetwork);
            if (network == null)
                return MethodResult.of(null, "Oh, this bad, missing network ...");
            List<Map<String, Object>> networkElementStates = new ArrayList<>();
            network.traverseElements(element -> {
                Map<String, Object> elementState = new HashMap<>();
                elementState.put("uuid", element.getUUID().toString());
                elementState.put("dimension", element.getDimension());
                if (world.isLoaded(element.getPos())) {
                    elementState.put("loaded", true);
                    TileEntity te = world.getBlockEntity(element.getPos());
                    if (te instanceof IEnderwireElement)
                        elementState.putAll(((IEnderwireElement<?>) te).getCurrentState());
                } else {
                    elementState.put("loaded", false);
                }
                networkElementStates.add(elementState);
            });
            return MethodResult.of(networkElementStates);
        });
    }
}
