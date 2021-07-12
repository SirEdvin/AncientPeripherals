package site.siredvin.ancientperipherals.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.BooleanProperty;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import site.siredvin.ancientperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.ancientperipherals.common.setup.TileEntityTypes;

import javax.annotation.Nullable;
import java.util.Objects;

public class FlexibleRealityAnchorTileEntity extends TileEntity {

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic;
    private @Nullable BlockState pendingState;

    public FlexibleRealityAnchorTileEntity() {
        super(TileEntityTypes.FLEXIBLE_REALITY_ANCHOR.get());
    }

    public void pushState() {
        if (pendingState != null) {
            setChanged();
            level.setBlockAndUpdate(getBlockPos(), pendingState);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            pendingState = null;
        }
    }

    public void setMimic(BlockState mimic) {
        this.mimic = mimic;
        if (pendingState == null)
            pendingState = getBlockState();
        pendingState = pendingState.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null);
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setBooleanStateValue(BooleanProperty stateValue, boolean value) {
        if (pendingState == null)
            pendingState = getBlockState();
        pendingState = pendingState.setValue(stateValue, value);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = mimic;
        CompoundNBT tag = pkt.getTag();
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(oldMimic, mimic)) {
                ModelDataManager.requestModelDataRefresh(this);
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
    }

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .build();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        if (tag.contains("mimic")) {
            mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if (mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        return super.save(tag);
    }

    public void clear() {
        this.setMimic(null);
    }

    private static CompoundNBT writeInteger(Integer tag) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.putString("number", tag.toString());
        return compoundnbt;
    }

    private static Integer readInteger(CompoundNBT tag) {
        if (!tag.contains("number", 8)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(tag.getString("number"));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
}
