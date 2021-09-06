package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TileEntityPeripheralOwner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireNetworkConnectorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;

import java.util.HashMap;
import java.util.Map;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetwork;
import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;

public class EnderwireNetworkConnectorPeripheral extends BasePeripheral<TileEntityPeripheralOwner<EnderwireNetworkConnectorTileEntity>> {
    public static final String TYPE = "enderwireNetworkConnector";

    public EnderwireNetworkConnectorPeripheral(EnderwireNetworkConnectorTileEntity tileEntity) {
        super(TYPE, new TileEntityPeripheralOwner<>(tileEntity));
    }

    @Override
    public boolean isEnabled() {
        return EnderwireElementType.MODEM.isEnabled();
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final String getAttachedNetworkName() {
        return owner.tileEntity.getAttachedNetwork();
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult inspectNetwork() throws LuaException {
        return withNetworks(getWorld(), data -> {
            String attachedNetwork = owner.tileEntity.getAttachedNetwork();
            if (attachedNetwork == null)
                return MethodResult.of(null, "Not attached to any network");
            EnderwireNetwork network = data.getNetwork(attachedNetwork);
            if (network == null)
                return MethodResult.of(null, "Oh, this bad, missing network ...");
            return MethodResult.of(NetworkRepresentationTool.fullRepresentation(network, getPos()));
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult getNetworkElementState(String name) throws LuaException {
        World world = getWorld();
        return withNetwork(world, owner.tileEntity, network -> {
            IEnderwireNetworkElement element = network.getElement(name);
            if (element == null)
                return MethodResult.of(null, "Cannot find element");
            Map<String, Object> elementState = new HashMap<>();
            elementState.put("name", element.getName());
            elementState.put("dimension", element.getDimension());
            if (world.isLoaded(element.getPos())) {
                elementState.put("loaded", true);
                TileEntity te = world.getBlockEntity(element.getPos());
                if (te instanceof IEnderwireElement)
                    elementState.putAll(((IEnderwireElement) te).getCurrentState());
            } else {
                elementState.put("loaded", false);
            }
            return MethodResult.of(elementState);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult configureElement(String name, Map<?, ?> configuration) throws LuaException {
        World world = getWorld();
        return withNetwork(world, owner.tileEntity, network -> {
            IEnderwireNetworkElement element = network.getElement(name);
            if (element == null)
                return MethodResult.of(null, "Cannot find element");
            if (!world.isLoaded(element.getPos()))
                return MethodResult.of(null, "Element is not loaded ...");
            if (!network.canReach(element, owner.getPos(), world.dimension().location().toString()))
                return MethodResult.of(null, "Cannot reach element");
            IEnderwireElement elementTE = element.getElement(world);
            if (elementTE == null)
                return MethodResult.of(null, "This shouldn't happen, but there is no tile entity");
            return elementTE.configure(configuration);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult configureElements(Map<?, ?> configurations) throws LuaException {
        World world = getWorld();
        return withNetwork(world, owner.tileEntity, network -> {
            for (Map.Entry<?, ?> configurationEntry: configurations.entrySet()) {
                Object nameKey = configurationEntry.getKey();
                Object configuration = configurationEntry.getValue();
                String elementName = nameKey.toString();
                if (!(configuration instanceof Map))
                    throw new LuaException("Configurations value should be maps");
                IEnderwireNetworkElement element = network.getElement(elementName);
                if (element == null)
                    return MethodResult.of(null, String.format("Cannot find element with name %s", elementName));
                if (!world.isLoaded(element.getPos()))
                    return MethodResult.of(null, "Element is not loaded ...");
                if (!network.canReach(element, owner.getPos(), world.dimension().location().toString()))
                    return MethodResult.of(null, "Cannot reach element");
                IEnderwireElement elementTE = element.getElement(world);
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

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult isElementLoaded(String name) throws LuaException  {
        World world = getWorld();
        return withNetwork(world, owner.tileEntity, network -> {
            IEnderwireNetworkElement element = network.getElement(name);
            if (element == null)
                return MethodResult.of(null, "Cannot find element");
            return MethodResult.of(world.isLoaded(element.getPos()));
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult isElementsInReach(String firstName, String secondName) throws LuaException {
        return withNetwork(getWorld(), owner.tileEntity, network -> {
            IEnderwireNetworkElement first = network.getElement(firstName);
            IEnderwireNetworkElement second = network.getElement(secondName);
            if (first == null)
                return MethodResult.of(null, String.format("Cannot find element %s", firstName));
            if (second == null)
                return MethodResult.of(null, String.format("Cannot find element %s", secondName));
            return MethodResult.of(network.canReach(first, second));
        });
    }
 }
