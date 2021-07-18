package site.siredvin.progressiveperipherals.common.tileentities;

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
import site.siredvin.progressiveperipherals.api.blocks.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;

import javax.annotation.Nullable;
import java.util.Objects;

public class FlexibleRealityAnchorTileEntity extends TileEntity implements ITileEntityDataProvider {

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

    public void setMimic(BlockState mimic, BlockState state) {
        this.mimic = mimic;
        if (pendingState == null)
            pendingState = state;
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
        tag = saveInternalData(tag);
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
            BlockState newMimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(mimic, newMimic)) {
                ModelDataManager.requestModelDataRefresh(this);
                setMimic(newMimic);
                pushState();
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
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (mimic != null) {
            data.put("mimic", NBTUtil.writeBlockState(mimic));
        }
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains("mimic")) {
            setMimic(NBTUtil.readBlockState(data.getCompound("mimic")), state);
            if (!skipUpdate)
                pushState();
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadInternalData(state, tag, true);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        return super.save(saveInternalData(tag));
    }

    public void clear() {
        this.setMimic(null);
    }
}
