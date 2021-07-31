package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GlobalNetworksData extends WorldSavedData {
    private static final String DATA_NAME = ProgressivePeripherals.MOD_ID + "_NetworkData";
    private static final String NETWORKS_TAG = "networks";

    private final Map<String, NetworkData> networks;

    public GlobalNetworksData() {
        super(DATA_NAME);
        networks = new HashMap<>();
    }

    public @Nullable NetworkData getNetwork(String name) {
        return networks.get(name);
    }

    public @Nullable NetworkData removeNetwork(String name, UUID playerUUID) {
        NetworkData network = getNetwork(name);
        if (network == null)
            return null;
        if (!playerUUID.equals(network.getOwnerUUID()))
            throw new IllegalArgumentException("Network cannot be removed by not owner!");
        return networks.remove(name);
    }

    public boolean isOwner(String name, UUID playerUUID) {
        NetworkData network = getNetwork(name);
        if (network == null)
            return false;
        return playerUUID.equals(network.getOwnerUUID());
    }

    public boolean networkExists(String name) {
        return networks.containsKey(name);
    }

    public List<NetworkData> getOwnerNetworks(UUID playerUUID) {
        return networks.values().stream().filter(network -> network.getOwnerUUID().equals(playerUUID)).collect(Collectors.toList());
    }

    public List<NetworkData> getVisibleNetworks(UUID playerUUID) {
        return networks.values().stream().filter(network -> network.getType() != NetworkType.PRIVATE || network.getOwnerUUID().equals(playerUUID)).collect(Collectors.toList());
    }

    public void addPublicNetwork(String name, UUID ownerUUID) {
        addNetwork(name, NetworkType.PUBLIC, ownerUUID, null);
    }

    public void addPrivateNetwork(String name, UUID ownerUUID) {
        addNetwork(name, NetworkType.PRIVATE, ownerUUID, null);
    }

    public void addEncryptedNetwork(String name, UUID ownerUUID, @NotNull String password) {
        addNetwork(name, NetworkType.ENCRYPTED, ownerUUID, password);
    }

    public void addNetwork(String name, NetworkType type, UUID ownerUUID, @Nullable String password) {
        if (networks.containsKey(name))
            throw new IllegalArgumentException("Cannot add network with same name!");
        NetworkData newNetwork = new NetworkData(name, type, ownerUUID, password);
        addNetworkWithoutUpdate(newNetwork);
        setDirty();
    }

    protected void addNetworkWithoutUpdate(NetworkData network) {
        networks.put(network.getName(), network);
    }

    @Override
    public void load(CompoundNBT tag) {
        ListNBT elements = tag.getList(NETWORKS_TAG, tag.getId());
        for (int i = 0; i < elements.size(); i++) {
            NetworkData network = new NetworkData();
            network.load(elements.getCompound(i));
            addNetworkWithoutUpdate(network);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        ListNBT serializedElements = new ListNBT();
        networks.values().forEach(element -> serializedElements.add(element.save(new CompoundNBT())));
        tag.put(NETWORKS_TAG, serializedElements);
        return tag;
    }

    public static @NotNull GlobalNetworksData get(@NotNull ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(GlobalNetworksData::new, DATA_NAME);
    }
}
