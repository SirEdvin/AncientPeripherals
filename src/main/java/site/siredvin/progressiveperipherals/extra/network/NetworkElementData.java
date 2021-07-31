package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class NetworkElementData {
    private static final String POS_TAG = "blockPosition";
    private static final String UUID_TAG = "uuid";
    private static final String ELEMENT_TYPE_TAG = "elementType";

    private final UUID uuid;
    private final BlockPos pos;
    private final EnderwireElementType elementType;

    public NetworkElementData(UUID uuid, BlockPos pos, EnderwireElementType elementType) {
        this.uuid = uuid;
        this.pos = pos;
        this.elementType = elementType;
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

    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString(UUID_TAG, uuid.toString());
        tag.put(POS_TAG, NBTUtil.writeBlockPos(pos));
        tag.putString(ELEMENT_TYPE_TAG, elementType.name().toUpperCase());
        return tag;
    }

    public static NetworkElementData fromCompound(CompoundNBT tag) {
        return new NetworkElementData(
                UUID.fromString(tag.getString(UUID_TAG)),
                NBTUtil.readBlockPos(tag.getCompound(POS_TAG)),
                EnderwireElementType.valueOf(tag.getString(ELEMENT_TYPE_TAG))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkElementData)) return false;
        NetworkElementData that = (NetworkElementData) o;
        return uuid.equals(that.uuid) && pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, pos);
    }
}
