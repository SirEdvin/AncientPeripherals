package site.siredvin.progressiveperipherals.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.BooleanProperty;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;

public class FlexibleRealityAnchorTileEntity extends MutableNBTTileEntity implements ITileEntityDataProvider {

    private static final String MIMIC_TAG = "mimic";
    private static final String LIGHT_LEVEL_TAG = "lightLevel";

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic;
    private int lightLevel = 0;
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

    public void setMimic(@Nullable BlockState mimic, @NotNull BlockState state, boolean skipUpdate) {
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

    public void setLightLevel(int lightLevel) {
        this.lightLevel = Math.max(0, Math.min(lightLevel, 15));
    }

    public int getLightLevel() {
        return lightLevel;
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
            data.put(MIMIC_TAG, NBTUtil.writeBlockState(mimic));
        }
        if (lightLevel != 0)
            data.putInt(LIGHT_LEVEL_TAG, lightLevel);
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains(MIMIC_TAG)) {
            setMimic(NBTUtil.readBlockState(data.getCompound(MIMIC_TAG)), state, true);
            if (!skipUpdate)
                pushState();
        }
        if (data.contains(LIGHT_LEVEL_TAG))
            setLightLevel(data.getInt(LIGHT_LEVEL_TAG));
    }
}
