package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkAccessingTool;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;

import java.util.stream.Collectors;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;

public class EnderwireNetworkManagerPeripheral extends BasePeripheral {

    public EnderwireNetworkManagerPeripheral(String type, IPocketAccess pocket) {
        super(type, pocket);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enderwireNetworkEnabled;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult createPublicNetwork(String name) throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            EnderwireNetwork existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            if (!data.isPlayerCanCreateNetworks(player.getUUID()))
                return MethodResult.of(null, "You cannot create more networks, delete some you owned first");
            data.addPublicNetwork(name, player.getUUID());
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult createPrivateNetwork(String name) throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            EnderwireNetwork existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            if (!data.isPlayerCanCreateNetworks(player.getUUID()))
                return MethodResult.of(null, "You cannot create more networks, delete some you owned first");
            data.addPrivateNetwork(name, player.getUUID());
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult createEncryptedNetwork(String name, String password) throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            EnderwireNetwork existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            if (!data.isPlayerCanCreateNetworks(player.getUUID()))
                return MethodResult.of(null, "You cannot create more networks, delete some you owned first");
            data.addEncryptedNetwork(name, player.getUUID(), password);
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult removeNetwork(String name) throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            EnderwireNetwork existingNetwork = data.getNetwork(name);
            if (existingNetwork == null)
                return MethodResult.of(null, "Cannot find network");
            if (!existingNetwork.getOwnerUUID().equals(player.getUUID()))
                return MethodResult.of(null, "You are not owner of this network");
            boolean removeNetworkResult = data.removeNetwork(name, player.getUUID()) != null;
            if (removeNetworkResult) {
                EnderwireNetwork selectedNetwork = NetworkAccessingTool.getSelectedNetwork(data, owner.getDataStorage());
                if (selectedNetwork != null && selectedNetwork.getName().equals(name))
                    NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), null);
            }
            return MethodResult.of(removeNetworkResult);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult selectNetwork(@NotNull IArguments arguments) throws LuaException {
        String name = arguments.getString(0);
        String password = arguments.optString(1, null);
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            Pair<MethodResult, EnderwireNetwork> accessResult = NetworkAccessingTool.accessNetwork(data, name, player, password);
            if (accessResult.leftPresent())
                return accessResult.getLeft();
            NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), accessResult.getRight());
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final boolean clearSelectedNetwork() {
        NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), null);
        return true;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSelectedNetwork() throws LuaException {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            EnderwireNetwork network = NetworkAccessingTool.getSelectedNetwork(data, owner.getDataStorage());
            if (network == null)
                return MethodResult.of((Object) null);
            return MethodResult.of(network.getName());
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getAvailableNetworks() throws LuaException {
        return withNetworks(
                getWorld(), owner.getOwner(),
                (data, player) -> MethodResult.of(data.getVisibleNetworks(player.getUUID()).stream().map(NetworkRepresentationTool::shortRepresentation).collect(Collectors.toList())));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getOwnedNetworks() throws LuaException {
        return withNetworks(
                getWorld(), owner.getOwner(),
                (data, player) -> MethodResult.of(data.getOwnerNetworks(player.getUUID()).stream().map(NetworkRepresentationTool::shortRepresentation).collect(Collectors.toList())));
    }
}
