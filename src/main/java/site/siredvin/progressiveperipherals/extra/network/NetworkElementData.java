package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class NetworkElementData {
    private static final String POS_TAG = "blockPosition";
    private static final String UUID_TAG = "uuid";

    private final UUID uuid;
    private final BlockPos pos;

    public NetworkElementData(UUID uuid, BlockPos pos) {
        this.uuid = uuid;
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public UUID getUUID() {
        return uuid;
    }

    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString(UUID_TAG, uuid.toString());
        tag.put(POS_TAG, NBTUtil.writeBlockPos(pos));
        return tag;
    }

    public static NetworkElementData fromCompound(CompoundNBT tag) {
        return new NetworkElementData(
                UUID.fromString(tag.getString(UUID_TAG)),
                NBTUtil.readBlockPos(tag.getCompound(POS_TAG))
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
