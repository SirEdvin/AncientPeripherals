package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.network;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.NetworkAccessingTool;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class NetworkManagerPeripheral extends OperationPeripheral {

    public NetworkManagerPeripheral(String type, IPocketAccess pocket) {
        super(type, pocket);
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.emptyList();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public MethodResult withNetworks(BiFunction<GlobalNetworksData, PlayerEntity, MethodResult> function) {
        ServerWorld world = (ServerWorld) getWorld();
        GlobalNetworksData data = GlobalNetworksData.get(world);
        PlayerEntity player = owner.getOwner();
        if (player == null)
            return MethodResult.of(null, "Cannot find player, that own this peripheral, cannot interact with networks ..");
        return function.apply(data, player);
    }

    @LuaFunction
    public final MethodResult createPublicNetwork(String name) {
        return withNetworks((data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            data.addPublicNetwork(name, player.getUUID());
            return MethodResult.of(true);
        });
    }

    @LuaFunction
    public final MethodResult createPrivateNetwork(String name) {
        return withNetworks((data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            data.addPrivateNetwork(name, player.getUUID());
            return MethodResult.of(true);
        });
    }

    @LuaFunction
    public final MethodResult createEncryptedNetwork(String name, String password) {
        return withNetworks((data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            data.addEncryptedNetwork(name, player.getUUID(), password);
            return MethodResult.of(true);
        });
    }

    @LuaFunction
    public final MethodResult selectNetwork(@NotNull IArguments arguments) throws LuaException {
        String name = arguments.getString(0);
        String password = arguments.optString(1, null);
        return withNetworks((data, player) -> {
            Pair<MethodResult, NetworkData> accessResult = NetworkAccessingTool.accessNetwork(data, name, player, password);
            if (accessResult.leftPresent())
                return accessResult.getLeft();
            NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), accessResult.getRight());
            return MethodResult.of(true);
        });
    }

    @LuaFunction
    public final MethodResult getSelectedNetwork() {
        return withNetworks((data, player) -> {
            NetworkData network = NetworkAccessingTool.getSelectedNetwork(data, owner.getDataStorage());
            if (network == null)
                return MethodResult.of();
            return MethodResult.of(network.getName());
        });
    }

    @LuaFunction
    public final MethodResult getAvailableNetworks() {
        return withNetworks((data, player) -> MethodResult.of(data.getVisibleNetworks(player.getUUID()).stream().map(NetworkData::getName).collect(Collectors.toList())));
    }

    @LuaFunction
    public final MethodResult getOwnedNetworks() {
        return withNetworks((data, player) -> MethodResult.of(data.getOwnerNetworks(player.getUUID()).stream().map(NetworkData::getName).collect(Collectors.toList())));
    }
}
