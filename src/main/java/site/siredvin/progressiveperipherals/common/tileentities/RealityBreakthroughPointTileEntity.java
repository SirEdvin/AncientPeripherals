package site.siredvin.progressiveperipherals.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import site.siredvin.progressiveperipherals.api.blocks.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.api.integrations.IProbeable;
import site.siredvin.progressiveperipherals.api.tileentity.IRealityBreakthroughPointTier;
import site.siredvin.progressiveperipherals.api.tileentity.IRealityBreakthroughPointTileEntity;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.utils.NBTUtils;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RealityBreakthroughPointTileEntity extends TileEntity implements IRealityBreakthroughPointTileEntity, ITileEntityDataProvider, IProbeable {

    private final static String COLOR_TAG = "pointColor";
    private final static String TIER_TAG = "pointTier";

    private @Nullable Color color;
    private @Nullable RealityBreakthroughPointTier tier;

    public RealityBreakthroughPointTileEntity() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_POINT.get());
    }


    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (color != null)
            data.put(COLOR_TAG, NBTUtils.serializer(color));
        if (tier != null)
            data.putString(TIER_TAG, tier.name().toLowerCase());
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains(COLOR_TAG))
            color = NBTUtils.readColor(data.getInt(COLOR_TAG));
        if (data.contains(TIER_TAG)) {
            try {
                tier = RealityBreakthroughPointTier.valueOf(data.getString(TIER_TAG).toUpperCase());
            } catch (IllegalArgumentException ignored) {
                tier = RealityBreakthroughPointTier.COMMON;
            }
        }
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

    @Override
    public @Nonnull Color getColor() {
        if (color == null)
            return Color.BLACK;
        return color;
    }

    @Override
    public @Nonnull IRealityBreakthroughPointTier getTier() {
        if (tier == null)
            return RealityBreakthroughPointTier.COMMON;
        return tier;
    }

    @Override
    public List<ITextComponent> commonProbeData(BlockState state) {
        List<ITextComponent> data = new ArrayList<>();
        data.add(TranslationUtil.localization("point_tier").append(getTier().name().toLowerCase()));
        return data;
    }
}
