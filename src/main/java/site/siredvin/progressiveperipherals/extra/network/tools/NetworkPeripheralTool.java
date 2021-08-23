package site.siredvin.progressiveperipherals.extra.network.tools;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.PeripheralBiFunction;
import site.siredvin.progressiveperipherals.extra.network.api.PeripheralFunction;

public class NetworkPeripheralTool {
    public static <T extends BasePeripheral> MethodResult withNetworks(World world, PlayerEntity owner, PeripheralBiFunction<GlobalNetworksData, PlayerEntity, MethodResult> function) throws LuaException {
        ServerWorld serverWorld = (ServerWorld) world;
        GlobalNetworksData data = GlobalNetworksData.get(serverWorld);
        if (owner == null)
            return MethodResult.of(null, "Cannot find player, that own this peripheral, cannot interact with networks ..");
        return function.apply(data, owner);
    }

    public static MethodResult withNetworks(World world, PeripheralFunction<GlobalNetworksData, MethodResult> function) throws LuaException {
        ServerWorld serverWorld = (ServerWorld) world;
        GlobalNetworksData data = GlobalNetworksData.get(serverWorld);
        return function.apply(data);
    }

    public static MethodResult withNetwork(World world, IEnderwireElement enderwireElement, PeripheralFunction<EnderwireNetwork, MethodResult> function) throws LuaException {
        ServerWorld serverWorld = (ServerWorld) world;
        GlobalNetworksData data = GlobalNetworksData.get(serverWorld);
        String attachedNetwork = enderwireElement.getAttachedNetwork();
        if (attachedNetwork == null)
            return MethodResult.of(null, "Not attached to any network");
        EnderwireNetwork network = data.getNetwork(attachedNetwork);
        if (network == null)
            return MethodResult.of(null, "Oh, this bad, missing network ...");
        return function.apply(network);
    }
}
