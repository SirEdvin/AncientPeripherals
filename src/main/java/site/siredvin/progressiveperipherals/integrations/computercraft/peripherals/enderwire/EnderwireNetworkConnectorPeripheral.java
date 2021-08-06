package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireNetworkListenerTileEntity;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.extra.network.NetworkElementData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;

import java.util.*;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;
import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetwork;

public class EnderwireNetworkConnectorPeripheral extends BasePeripheral {
    private final EnderwireNetworkListenerTileEntity tileEntity;
    public EnderwireNetworkConnectorPeripheral(String type, EnderwireNetworkListenerTileEntity tileEntity) {
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
    public final MethodResult inspectNetwork() throws LuaException {
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
    public final MethodResult getNetworkElementState(String uuid) throws LuaException {
        World world = getWorld();
        return withNetwork(getWorld(), tileEntity, network -> {
            NetworkElementData element = network.getElement(UUID.fromString(uuid));
            if (element == null)
                return MethodResult.of(null, "Cannot find element");
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
            return MethodResult.of(elementState);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult configureElement(String elementUUID, Map<?, ?> configuration) throws LuaException {
        World world = getWorld();
        return withNetwork(getWorld(), tileEntity, network -> {
            NetworkElementData element = network.getElement(UUID.fromString(elementUUID));
            if (element == null)
                return MethodResult.of(null, "Cannot find element");
            if (!world.isLoaded(element.getPos()))
                return MethodResult.of(null, "Element is not loaded ...");
            IEnderwireElement<?> elementTE = (IEnderwireElement<?>) world.getBlockEntity(element.getPos());
            if (elementTE == null)
                return MethodResult.of(null, "This shouldn't happen, but there is no tile entity");
            return elementTE.configure(configuration);
        });
    }

    @LuaFunction
    public final MethodResult isElementLoaded(String elementUUID) throws LuaException  {
        World world = getWorld();
        return withNetwork(getWorld(), tileEntity, network -> {
            NetworkElementData element = network.getElement(UUID.fromString(elementUUID));
            if (element == null)
                return MethodResult.of(null, "Cannot find element");
            return MethodResult.of(world.isLoaded(element.getPos()));
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult configureElements(Map<?, ?> configurations) throws LuaException {
        World world = getWorld();
        return withNetwork(getWorld(), tileEntity, network -> {
            for (Map.Entry<?, ?> configurationEntry: configurations.entrySet()) {
                Object uuidKey = configurationEntry.getKey();
                Object configuration = configurationEntry.getValue();
                UUID elementUUID;
                try {
                    elementUUID = UUID.fromString(uuidKey.toString());
                } catch (IllegalArgumentException ignored) {
                    throw new LuaException("Configurations key should be element UUID");
                }
                if (!(configuration instanceof Map))
                    throw new LuaException("Configurations value should be maps");
                NetworkElementData element = network.getElement(elementUUID);
                if (element == null)
                    return MethodResult.of(null, String.format("Cannot find element with uuid %s", elementUUID));
                if (!world.isLoaded(element.getPos()))
                    return MethodResult.of(null, "Element is not loaded ...");
                IEnderwireElement<?> elementTE = (IEnderwireElement<?>) world.getBlockEntity(element.getPos());
                if (elementTE == null)
                    return MethodResult.of(null, "This shouldn't happen, but there is no tile entity");
                MethodResult configureResult = elementTE.configure((Map<?, ?>) configuration);
                Object[] result = configureResult.getResult();
                if (result == null || result.length == 0)
                    return MethodResult.of(null, "Blame developer, incorrect method implementation here");
                if (result[0] == null)
                    return configureResult;
            }
            return MethodResult.of(true);
        });
    }
 }
