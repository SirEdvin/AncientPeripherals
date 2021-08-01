package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkType;

import java.util.*;
import java.util.stream.Collectors;

public class GlobalNetworksData extends WorldSavedData {
    private static final String DATA_NAME = ProgressivePeripherals.MOD_ID + "_NetworkData";
    private static final String NETWORKS_TAG = "networks";

    private final Map<String, NetworkData> networks;
    private ServerWorld serverWorld;

    public GlobalNetworksData() {
        super(DATA_NAME);
        networks = new HashMap<>();
    }

    public void setServerWorld(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
    }

    public @Nullable NetworkData getNetwork(String name) {
        return networks.get(name);
    }

    public @Nullable NetworkData removeNetwork(String name, UUID playerUUID) {
        Objects.requireNonNull(serverWorld);
        NetworkData network = getNetwork(name);
        if (network == null)
            return null;
        if (!playerUUID.equals(network.getOwnerUUID()))
            throw new IllegalArgumentException("Network cannot be removed by not owner!");
        Map<UUID, NetworkElementData> elements = network.getElements();
        if (elements != null) {
            List<NetworkElementData> removeTargets = new ArrayList<>();
            for (NetworkElementData networkElement : elements.values()) {
                if (serverWorld.isLoaded(networkElement.getPos())) {
                    removeTargets.add(networkElement);
                }
            }
            removeTargets.forEach(networkElement -> {
                IEnderwireElement<?> enderwireElement = (IEnderwireElement<?>) serverWorld.getBlockEntity(networkElement.getPos());
                if (enderwireElement != null) {
                    enderwireElement.changeAttachedNetwork(null);
                } else {
                    throw new IllegalArgumentException("Network remove logic should be called from world thread!");
                }
            });
        }
        NetworkData removed = networks.remove(name);
        setDirty();
        return removed;
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
        GlobalNetworksData instance = world.getDataStorage().computeIfAbsent(GlobalNetworksData::new, DATA_NAME);
        instance.setServerWorld(world);
        return instance;
    }
}
