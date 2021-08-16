package site.siredvin.progressiveperipherals.extra.network.tools;

import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;

public class NetworkAccessingTool {
    private static final String SELECTED_NETWORK_TAG = "selectedNetwork";

    public static Pair<MethodResult, EnderwireNetwork> accessNetwork(GlobalNetworksData data, String name, PlayerEntity player, @Nullable String password) {
        EnderwireNetwork network = data.getNetwork(name);
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

    public static void writeSelectedNetwork(CompoundNBT tag, @Nullable EnderwireNetwork network) {
        if (network == null) {
            tag.remove(SELECTED_NETWORK_TAG);
        } else {
            tag.putString(SELECTED_NETWORK_TAG, network.getName());
        }
    }

    public static void writeSelectedNetwork(CompoundNBT tag, @Nullable String name) {
        if (name == null) {
            tag.remove(SELECTED_NETWORK_TAG);
        } else {
            tag.putString(SELECTED_NETWORK_TAG, name);
        }
    }

    public static @Nullable EnderwireNetwork getSelectedNetwork(GlobalNetworksData data, CompoundNBT tag) {
        String attachedNetworkName = getSelectedNetworkName(tag);
        if (attachedNetworkName == null)
            return null;
        return data.getNetwork(attachedNetworkName);
    }

    public static @Nullable String getSelectedNetworkName(CompoundNBT tag) {
        if (!tag.contains(SELECTED_NETWORK_TAG))
            return null;
        return tag.getString(SELECTED_NETWORK_TAG);
    }

    public static boolean isSelectedNetworkPresent(CompoundNBT tag) {
        return tag.contains(SELECTED_NETWORK_TAG);
    }
}
