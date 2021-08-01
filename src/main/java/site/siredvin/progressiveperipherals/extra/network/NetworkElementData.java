package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;

import java.util.Objects;
import java.util.UUID;

public class NetworkElementData {
    private static final String POS_TAG = "blockPosition";
    private static final String UUID_TAG = "uuid";
    private static final String ELEMENT_TYPE_TAG = "elementType";
    private static final String DEVICE_TYPE_TAG = "deviceType";

    private final UUID uuid;
    private final BlockPos pos;
    private final EnderwireElementType elementType;
    private final String deviceType;
    private final String dimension;

    public NetworkElementData(UUID uuid, BlockPos pos, EnderwireElementType elementType, String deviceType, String dimension) {
        this.uuid = uuid;
        this.pos = pos;
        this.elementType = elementType;
        this.deviceType = deviceType;
        this.dimension = dimension;
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

    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString(UUID_TAG, uuid.toString());
        tag.put(POS_TAG, NBTUtil.writeBlockPos(pos));
        tag.putString(ELEMENT_TYPE_TAG, elementType.name().toUpperCase());
        tag.putString(DEVICE_TYPE_TAG, deviceType);
        return tag;
    }

    public static NetworkElementData fromCompound(CompoundNBT tag) {
        return new NetworkElementData(
                UUID.fromString(tag.getString(UUID_TAG)),
                NBTUtil.readBlockPos(tag.getCompound(POS_TAG)),
                EnderwireElementType.valueOf(tag.getString(ELEMENT_TYPE_TAG)),
                tag.getString(DEVICE_TYPE_TAG),
                "kekwhat"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkElementData)) return false;
        NetworkElementData that = (NetworkElementData) o;
        return uuid.equals(that.uuid) && pos.equals(that.pos) && elementType.equals(that.elementType) && deviceType.equals(that.deviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, pos, elementType, deviceType);
    }
}
