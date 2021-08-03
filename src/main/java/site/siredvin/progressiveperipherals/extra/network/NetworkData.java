package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class NetworkData {
    private static final String TYPE_TAG = "type";
    private static final String NAME_TAG = "name";
    private static final String OWNER_UUID_TAG = "ownerUUID";
    private static final String PASSWORD_TAG = "password";
    private static final String SALT_TAG = "salt";
    private static final String ELEMENTS_TAG = "elements";

    private NetworkType type;
    private String name;
    private UUID ownerUUID;
    private @Nullable String password;
    private @Nullable String salt;
    private @Nullable Map<UUID, NetworkElementData> elements;

    private int reachableRange;
    private boolean interdimensional;

    protected NetworkData(String name, NetworkType type, UUID ownerUUID, @Nullable String password) {
        this.type = type;
        this.ownerUUID = ownerUUID;
        this.name = name;
        if (password != null)
            setPassword(password);
        recalculateNetworkStats();
    }

    protected NetworkData(String name, NetworkType type, UUID ownerUUID) {
        this(name, type, ownerUUID, null);
    }

    protected NetworkData() {
        this("temporary_dummy", NetworkType.PUBLIC, Util.NIL_UUID);
    }

    public String getName() {
        return name;
    }

    public NetworkType getType() {
        return type;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public int getReachableRange() {
        return reachableRange;
    }

    public boolean isInterdimensional() {
        return interdimensional;
    }

    protected void recalculateNetworkStats() {
        reachableRange = ProgressivePeripheralsConfig.enderwireNetworkRangeStep;
        interdimensional = false;
        if (elements != null) {
            elements.values().forEach(networkElement -> {
                switch (networkElement.getNetworkAmplifier()) {
                    case EXTEND_RANGE:
                        reachableRange += ProgressivePeripheralsConfig.enderwireNetworkRangeStep;
                        break;
                    case MAKE_INTERDIMENSIONAL:
                        interdimensional = true;
                        break;
                }
            });
        }
    }

    public boolean testPassword(@Nullable String password) {
        if (this.password == null)
            return true;
        if (password == null)
            return false;
        return PasswordUtil.verifyUserPassword(password, this.password, this.salt);
    }

    public void setPassword(@NotNull String password) {
        if (this.salt == null)
            this.salt = PasswordUtil.generateNewSalt();
        this.password = PasswordUtil.generateSecurePassword(password, this.salt);
    }

    public @Nullable NetworkElementData getElement(UUID elementUUID) {
        if (elements == null)
            return null;
        return elements.get(elementUUID);
    }

    public @Nullable Map<UUID, NetworkElementData> getElements() {
        return elements;
    }

    public void traverseElements(Consumer<NetworkElementData> consumer) {
        if (elements != null)
            elements.values().forEach(consumer);
    }

    protected void addNetworkElementNoEvent(NetworkElementData element) {
        if (elements == null)
            elements = new HashMap<>();
        elements.put(element.getUUID(), element);
    }

    public void addNetworkElement(NetworkElementData element) {
        addNetworkElementNoEvent(element);
        recalculateNetworkStats();
        EnderwireNetworkBusHub.fireNetworkEvent(name, EnderwireNetworkEvent.addedElements(element));
    }

    public @Nullable NetworkElementData removeNetworkElement(NetworkElementData element) {
        return removeNetworkElementByUUID(element.getUUID());
    }

    public @Nullable NetworkElementData removeNetworkElementByUUID(UUID uuid) {
        if (elements != null) {
            NetworkElementData removed = elements.remove(uuid);
            if (removed != null) {
                recalculateNetworkStats();
                EnderwireNetworkBusHub.fireNetworkEvent(name, EnderwireNetworkEvent.removedElements(removed));
            }
            return removed;
        }
        return null;
    }

    public void load(CompoundNBT tag) {
        type = NetworkType.valueOf(tag.getString(TYPE_TAG));
        ownerUUID = UUID.fromString(tag.getString(OWNER_UUID_TAG));
        name = tag.getString(NAME_TAG);
        if (tag.contains(PASSWORD_TAG))
            password = tag.getString(PASSWORD_TAG);
        if (tag.contains(SALT_TAG))
            salt = tag.getString(SALT_TAG);
        if (tag.contains(ELEMENTS_TAG)) {

            ListNBT elements = tag.getList(ELEMENTS_TAG, tag.getId());
            for (int i = 0; i < elements.size(); i++) {
                addNetworkElementNoEvent(NetworkElementData.fromCompound(elements.getCompound(i)));
            }
            recalculateNetworkStats();
        }
    }

    public CompoundNBT save(CompoundNBT tag) {
        tag.putString(TYPE_TAG, type.toString().toUpperCase());
        tag.putString(OWNER_UUID_TAG, ownerUUID.toString());
        tag.putString(NAME_TAG, name);
        if (password != null)
            tag.putString(PASSWORD_TAG, password);
        if (salt != null)
            tag.putString(SALT_TAG, salt);
        if (elements != null) {
            ListNBT serializedElements = new ListNBT();
            elements.values().forEach(element -> serializedElements.add(element.toNBT()));
            tag.put(ELEMENTS_TAG, serializedElements);
        }
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkData)) return false;
        NetworkData that = (NetworkData) o;
        return type == that.type && name.equals(that.name) && ownerUUID.equals(that.ownerUUID) && Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, ownerUUID, elements);
    }
}
