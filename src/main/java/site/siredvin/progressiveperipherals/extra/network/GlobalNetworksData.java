package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;

import java.util.*;
import java.util.stream.Collectors;

public class GlobalNetworksData extends WorldSavedData {
    private static final String DATA_NAME = ProgressivePeripherals.MOD_ID + "_NetworkData";
    private static final String NETWORKS_TAG = "networks";

    private final Map<String, EnderwireNetwork> networks;
    private ServerWorld serverWorld;

    public GlobalNetworksData() {
        super(DATA_NAME);
        networks = new HashMap<>();
    }

    public void setServerWorld(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
    }

    public @Nullable EnderwireNetwork getNetwork(String name) {
        return networks.get(name);
    }

    public @Nullable EnderwireNetwork removeNetwork(String name, UUID playerUUID) {
        Objects.requireNonNull(serverWorld);
        EnderwireNetwork network = getNetwork(name);
        if (network == null)
            return null;
        if (!playerUUID.equals(network.getOwnerUUID()))
            throw new IllegalArgumentException("Network cannot be removed by not owner!");
        Map<String, EnderwireNetworkElement> elements = network.getElements();
        if (elements != null) {
            List<EnderwireNetworkElement> removeTargets = new ArrayList<>();
            for (EnderwireNetworkElement networkElement : elements.values()) {
                if (serverWorld.isLoaded(networkElement.getPos())) {
                    removeTargets.add(networkElement);
                }
            }
            removeTargets.forEach(networkElement -> {
                BlockPos pos = networkElement.getPos();
                if (!serverWorld.isEmptyBlock(pos)) {
                    IEnderwireElement<?> enderwireElement = (IEnderwireElement<?>) serverWorld.getBlockEntity(networkElement.getPos());
                    if (enderwireElement != null) {
                        enderwireElement.changeAttachedNetwork(null);
                    } else {
                        throw new IllegalArgumentException("Network remove logic should be called from world thread!");
                    }
                }
            });
        }
        EnderwireNetworkBusHub.removeNetworkData(name);
        EnderwireNetwork removed = networks.remove(name);
        setDirty();
        return removed;
    }

    public boolean networkExists(String name) {
        return networks.containsKey(name);
    }

    public List<EnderwireNetwork> getOwnerNetworks(UUID playerUUID) {
        return networks.values().stream().filter(network -> network.getOwnerUUID().equals(playerUUID)).collect(Collectors.toList());
    }

    public List<EnderwireNetwork> getVisibleNetworks(UUID playerUUID) {
        return networks.values().stream().filter(network -> network.getType() != NetworkType.PRIVATE || network.getOwnerUUID().equals(playerUUID)).collect(Collectors.toList());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isPlayerCanCreateNetworks(UUID playerUUID) {
        long ownedNetworkCount = networks.values().stream().filter(network -> network.getOwnerUUID().equals(playerUUID)).count();
        return ownedNetworkCount < ProgressivePeripheralsConfig.enderwireNetworkMaxCountPerPlayer;
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
        if (!isPlayerCanCreateNetworks(ownerUUID))
            throw new IllegalArgumentException("Player reached network limit");
        EnderwireNetwork newNetwork = new EnderwireNetwork(name, type, ownerUUID, password);
        addNetworkWithoutUpdate(newNetwork);
        setDirty();
    }

    protected void addNetworkWithoutUpdate(EnderwireNetwork network) {
        networks.put(network.getName(), network);
    }

    @Override
    public void load(CompoundNBT tag) {
        ListNBT elements = tag.getList(NETWORKS_TAG, tag.getId());
        for (int i = 0; i < elements.size(); i++) {
            EnderwireNetwork network = new EnderwireNetwork();
            network.load(elements.getCompound(i));
            addNetworkWithoutUpdate(network);
        }
    }

    @Override
    public @NotNull CompoundNBT save(CompoundNBT tag) {
        ListNBT serializedElements = new ListNBT();
        networks.values().forEach(element -> serializedElements.add(element.save(new CompoundNBT())));
        tag.put(NETWORKS_TAG, serializedElements);
        return tag;
    }

    public static @NotNull GlobalNetworksData get(@NotNull ServerWorld world) {
        GlobalNetworksData instance = world.getServer().overworld().getDataStorage().computeIfAbsent(GlobalNetworksData::new, DATA_NAME);
        instance.setServerWorld(world);
        return instance;
    }
}
