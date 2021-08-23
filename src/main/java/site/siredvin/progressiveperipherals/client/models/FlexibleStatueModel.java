package site.siredvin.progressiveperipherals.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.client.models.abstr.AbstractFlexibleStatueModel;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.util.List;
import java.util.Random;

public class FlexibleStatueModel extends AbstractFlexibleStatueModel implements IDynamicBakedModel {
    private final FlexibleStatueItemOverrideList overrides = new FlexibleStatueItemOverrideList();


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        QuadList quadsData = extraData.getData(FlexibleStatueTileEntity.BAKED_QUADS);
        return getQuads(state, side, rand, quadsData);
    }

    @Override
    public @NotNull ItemOverrideList getOverrides() {
        return overrides;
    }
}
