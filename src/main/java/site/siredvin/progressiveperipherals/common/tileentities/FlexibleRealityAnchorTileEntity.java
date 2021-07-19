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
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class FlexibleRealityAnchorTileEntity extends MutableNBTTileEntity implements ITileEntityDataProvider {

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic;
    private @Nullable BlockState pendingState;

    public FlexibleRealityAnchorTileEntity() {
        super(TileEntityTypes.FLEXIBLE_REALITY_ANCHOR.get());
    }

    public void pushStackedState() {
        if (pendingState != null) {
            pushState(pendingState);
            pendingState = null;
        }
    }

    public void setMimic(@Nullable BlockState mimic, boolean skipUpdate) {
        setMimic(mimic, getBlockState(), skipUpdate);
    }

    public void setMimic(@Nullable BlockState mimic, @Nonnull BlockState state, boolean skipUpdate) {
        this.mimic = mimic;
        if (!skipUpdate) {
            pushState(state.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null));
        } else {
            if (pendingState == null)
                pendingState = state;
            pendingState = pendingState.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null);
        }
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
            setMimic(NBTUtil.readBlockState(data.getCompound("mimic")), state, true);
            if (!skipUpdate)
                pushState();
        }
    }
}
