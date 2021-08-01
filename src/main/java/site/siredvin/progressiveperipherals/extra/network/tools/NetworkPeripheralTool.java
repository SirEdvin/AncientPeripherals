package site.siredvin.progressiveperipherals.extra.network.tools;

import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;

import java.util.function.BiFunction;
import java.util.function.Function;

public class NetworkPeripheralTool {
    public static <T extends BasePeripheral> MethodResult withNetworks(World world, PlayerEntity owner, BiFunction<GlobalNetworksData, PlayerEntity, MethodResult> function) {
        ServerWorld serverWorld = (ServerWorld) world;
        GlobalNetworksData data = GlobalNetworksData.get(serverWorld);
        if (owner == null)
            return MethodResult.of(null, "Cannot find player, that own this peripheral, cannot interact with networks ..");
        return function.apply(data, owner);
    }

    public static <T extends BasePeripheral> MethodResult withNetworks(World world, Function<GlobalNetworksData, MethodResult> function) {
        ServerWorld serverWorld = (ServerWorld) world;
        GlobalNetworksData data = GlobalNetworksData.get(serverWorld);
        return function.apply(data);
    }
}
