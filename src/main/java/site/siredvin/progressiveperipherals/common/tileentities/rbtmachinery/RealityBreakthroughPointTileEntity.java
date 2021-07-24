package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IProbeable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RealityBreakthroughPointTileEntity extends TileEntity implements ITileEntityDataProvider, IProbeable {

    private static final String POINT_STATE_TAG = "state";

    private final @NotNull PointState pointState = new PointState(RealityBreakthroughPointTier.COMMON);
    private final @NotNull RealityBreakthroughPointType pointType = RealityBreakthroughPointType.IRREALIUM;

    public RealityBreakthroughPointTileEntity() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_POINT.get());
        setTier(RealityBreakthroughPointTier.COMMON);
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data.put(POINT_STATE_TAG, pointState.serializeNBT());
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains(POINT_STATE_TAG))
            pointState.deserializeNBT(data.getCompound(POINT_STATE_TAG));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        return saveInternalData(super.save(tag));
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadInternalData(state, tag, true);
    }

    public @NotNull Color getColor() {
        return pointState.getColor();
    }

    public @NotNull RealityBreakthroughPointTier getTier() {
        return pointState.getTier();
    }

    @Override
    public List<ITextComponent> commonProbeData(BlockState state) {
        List<ITextComponent> data = new ArrayList<>();
        data.add(TranslationUtil.localization("point_tier").append(getTier().name().toLowerCase()));
        return data;
    }

    public void setTier(@NotNull RealityBreakthroughPointTier tier) {
        if (level != null) {
            this.pointState.setTier(tier, level.getRandom());
        } else {
            this.pointState.setTier(tier);
        }
    }

    public boolean isDecrypted() {
        return pointState.isDecrypted();
    }

    public boolean canProduceResource() {
        return !pointState.isDecrypted() && pointType.getProducibleItem() != null;
    }

    public @Nullable Item getProducibleResource() {
        if (pointState.isDecrypted)
            return null;
        return pointType.getProducibleItem();
    }

    public static class PointState {
        private final static String TIER_TAG = "pointTier";
        private final static String POWER_LEVEL_TAG = "powerLevel";
        private final static String IS_DECRYPTED_TAG = "isDecrypted";

        private int powerLevel;
        private boolean isDecrypted;
        private RealityBreakthroughPointTier tier;

        public PointState(RealityBreakthroughPointTier tier) {
            this.tier = tier;
            this.powerLevel = tier.getPowerLevel();
            this.isDecrypted = false;
        }

        public boolean isDecrypted() {
            return isDecrypted;
        }

        public void setDecrypted(boolean decrypted) {
            isDecrypted = decrypted;
        }

        public RealityBreakthroughPointTier getTier() {
            return tier;
        }

        public Color getColor() {
            return tier.getColor();
        }

        public void setTier(RealityBreakthroughPointTier tier) {
            setTier(tier, new Random());
        }

        public void setTier(RealityBreakthroughPointTier tier, Random random) {
            this.tier = tier;
            this.powerLevel = tier.getPowerLevel(random);
            this.isDecrypted = false;
        }

        public int getPowerLevel() {
            return powerLevel;
        }

        public void consumePower(int amount) {
            powerLevel-=amount;
        }

        public CompoundNBT serializeNBT() {
            CompoundNBT data = new CompoundNBT();
            data.putInt(POWER_LEVEL_TAG, powerLevel);
            data.putString(TIER_TAG, tier.name().toLowerCase());
            data.putBoolean(IS_DECRYPTED_TAG, isDecrypted);
            return data;
        }

        public void deserializeNBT(CompoundNBT data) {
            this.tier = RealityBreakthroughPointTier.valueOf(data.getString(TIER_TAG).toUpperCase());
            this.powerLevel = data.getInt(POWER_LEVEL_TAG);
            this.isDecrypted = data.getBoolean(IS_DECRYPTED_TAG);
        }
    }
}
