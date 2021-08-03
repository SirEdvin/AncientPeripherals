package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkAmplifier;

import java.util.Objects;
import java.util.UUID;

public class NetworkElementData {
    private static final String POS_TAG = "blockPosition";
    private static final String UUID_TAG = "uuid";
    private static final String ELEMENT_TYPE_TAG = "elementType";
    private static final String DEVICE_TYPE_TAG = "deviceType";
    private static final String DIMENSION_TAG = "dimension";
    private static final String NETWORK_AMPLIFIER_TAG = "networkAmplifier";

    private final @NotNull UUID uuid;
    private final @NotNull BlockPos pos;
    private final @NotNull EnderwireElementType elementType;
    private final @NotNull String deviceType;
    private final @NotNull String dimension;
    private final @NotNull NetworkAmplifier networkAmplifier;

    public NetworkElementData(@NotNull UUID uuid, @NotNull BlockPos pos, @NotNull EnderwireElementType elementType, @NotNull String deviceType, @NotNull String dimension, @NotNull NetworkAmplifier networkAmplifier) {
        this.uuid = uuid;
        this.pos = pos;
        this.elementType = elementType;
        this.deviceType = deviceType;
        this.dimension = dimension;
        this.networkAmplifier = networkAmplifier;
    }

    public BlockPos getPos() {
        return pos;
    }

    public UUID getUUID() {
        return uuid;
    }

    public EnderwireElementType getElementType() {
        return elementType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public NetworkAmplifier getNetworkAmplifier() {
        return networkAmplifier;
    }

    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString(UUID_TAG, uuid.toString());
        tag.put(POS_TAG, NBTUtil.writeBlockPos(pos));
        tag.putString(ELEMENT_TYPE_TAG, elementType.name().toUpperCase());
        tag.putString(DEVICE_TYPE_TAG, deviceType);
        tag.putString(DIMENSION_TAG, dimension);
        tag.putString(NETWORK_AMPLIFIER_TAG, networkAmplifier.name());
        return tag;
    }

    public static NetworkElementData fromCompound(CompoundNBT tag) {
        return new NetworkElementData(
                UUID.fromString(tag.getString(UUID_TAG)),
                NBTUtil.readBlockPos(tag.getCompound(POS_TAG)),
                EnderwireElementType.valueOf(tag.getString(ELEMENT_TYPE_TAG)),
                tag.getString(DEVICE_TYPE_TAG),
                tag.getString(DIMENSION_TAG),
                NetworkAmplifier.valueOf(tag.getString(NETWORK_AMPLIFIER_TAG))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkElementData)) return false;
        NetworkElementData that = (NetworkElementData) o;
        return uuid.equals(that.uuid) && pos.equals(that.pos) && elementType.equals(that.elementType) && deviceType.equals(that.deviceType) && dimension.equals(that.dimension) && networkAmplifier == that.networkAmplifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, pos, elementType, deviceType, dimension, networkAmplifier);
    }
}
