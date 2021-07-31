package site.siredvin.progressiveperipherals.extra.network;

import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.Nullable;

public class NetworkAccessingTool {
    private static final String SELECTED_NETWORK_TAG = "selectedNetwork";

    public static Pair<MethodResult, NetworkData> accessNetwork(GlobalNetworksData data, String name, PlayerEntity player, @Nullable String password) {
        NetworkData network = data.getNetwork(name);
        if (network == null)
            return Pair.onlyLeft(MethodResult.of(null, "Cannot find network"));
        switch (network.getType()) {
            case PUBLIC:
                return Pair.onlyRight(network);
            case PRIVATE:
                if (player.getUUID().equals(network.getOwnerUUID()))
                    return Pair.onlyRight(network);
                return Pair.onlyLeft(MethodResult.of(null, "Cannot find network"));
            case ENCRYPTED:
                if (network.testPassword(password))
                    return Pair.onlyRight(network);
        }
        return Pair.onlyLeft(MethodResult.of(null, "Cannot get access to network"));
    }

    public static void writeSelectedNetwork(CompoundNBT tag, NetworkData network) {
        tag.putString(SELECTED_NETWORK_TAG, network.getName());
    }

    public static @Nullable NetworkData getSelectedNetwork(GlobalNetworksData data, CompoundNBT tag) {
        return data.getNetwork(tag.getString(SELECTED_NETWORK_TAG));
    }
}
