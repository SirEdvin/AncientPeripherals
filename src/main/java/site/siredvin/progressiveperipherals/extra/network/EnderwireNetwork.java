package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementSerializedTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class EnderwireNetwork {
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
    private @Nullable Map<String, IEnderwireNetworkElement> elements;

    private int reachableRange;
    private boolean interdimensional;

    protected EnderwireNetwork(String name, NetworkType type, UUID ownerUUID, @Nullable String password) {
        this.type = type;
        this.ownerUUID = ownerUUID;
        this.name = name;
        if (password != null)
            setPassword(password);
        recalculateNetworkStats();
    }

    protected EnderwireNetwork() {
        this("temporary_dummy", NetworkType.PUBLIC, Util.NIL_UUID, null);
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

    public boolean canAcceptNewElements() {
        if (elements != null) {
            return ProgressivePeripheralsConfig.enderwireNetworkMaxElementCount > elements.size();
        }
        return true;
    }

    public boolean canReach(IEnderwireNetworkElement first, IEnderwireNetworkElement second) {
        return canReach(reachableRange, interdimensional, first.getPos(), second.getPos(), first.getDimension(), second.getDimension());
    }

    public boolean canReach(IEnderwireNetworkElement element, BlockPos target, String targetDimension) {
        return canReach(reachableRange, interdimensional, element.getPos(), target, element.getDimension(), targetDimension);
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

    public @Nullable IEnderwireNetworkElement getElement(String name) {
        if (elements == null)
            return null;
        return elements.get(name);
    }

    public @Nullable Map<String, IEnderwireNetworkElement> getElements() {
        return elements;
    }

    public void traverseElements(Consumer<IEnderwireNetworkElement> consumer) {
        if (elements != null)
            elements.values().forEach(consumer);
    }

    public String generateNameForElement(@NotNull IEnderwireElement element) {
        String elementTypeName = element.getElementType().lowerTitleCase();
        int lastIndex = 0;
        if (elements != null)
            lastIndex = elements.values().stream().filter(el -> el.getName().startsWith(elementTypeName)).map(el -> Integer.valueOf(el.getName().split("_")[1])).max(Integer::compareTo).orElse(lastIndex);
        lastIndex++; // Move to next index
        return elementTypeName + "_" + lastIndex;
    }

    protected void addNetworkElementNoEvent(IEnderwireNetworkElement element) {
        if (elements == null)
            elements = new HashMap<>();
        elements.put(element.getName(), element);
    }

    public void addNetworkElement(IEnderwireNetworkElement element) {
        addNetworkElementNoEvent(element);
        recalculateNetworkStats();
        EnderwireNetworkBusHub.fireNetworkEvent(name, new EnderwireNetworkEvent.ElementAdded(element));
    }


    @SuppressWarnings("UnusedReturnValue")
    public @Nullable IEnderwireNetworkElement removeNetworkElement(IEnderwireNetworkElement element) {
        return removeNetworkElementByName(element.getName());
    }

    public @Nullable IEnderwireNetworkElement removeNetworkElementByName(String name) {
        if (elements != null) {
            IEnderwireNetworkElement removed = elements.remove(name);
            if (removed != null) {
                recalculateNetworkStats();
                EnderwireNetworkBusHub.fireNetworkEvent(this.name, new EnderwireNetworkEvent.ElementRemoved(removed));
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
                addNetworkElementNoEvent(NetworkElementSerializedTool.deserialize(elements.getCompound(i)));
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
            elements.values().stream().filter(IEnderwireNetworkElement::isStable).forEach(element -> serializedElements.add(NetworkElementSerializedTool.serialize(element)));
            tag.put(ELEMENTS_TAG, serializedElements);
        }
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnderwireNetwork)) return false;
        EnderwireNetwork that = (EnderwireNetwork) o;
        return type == that.type && name.equals(that.name) && ownerUUID.equals(that.ownerUUID) && Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, ownerUUID, elements);
    }

    public static boolean canReach(int reachableRange, boolean interdimensional, BlockPos source, BlockPos target, String sourceDimension, String targetDimension) {
        if (interdimensional)
            return true;
        if (!sourceDimension.equals(targetDimension))
            return false;
        final int reachableRangeSqr = reachableRange * reachableRange;
        return reachableRangeSqr >= source.distSqr(target);
    }
}
