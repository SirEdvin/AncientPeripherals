package site.siredvin.progressiveperipherals.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleRealityAnchorTileEntity;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FlexibleRealityAnchorModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/glass");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(TEXTURE);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        BlockState mimic = extraData.getData(FlexibleRealityAnchorTileEntity.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FlexibleRealityAnchor)) {
            IBakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
            return model.getQuads(state, side, rand, extraData);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return getTexture();
    }

    @Override
    public @NotNull ItemCameraTransforms getTransforms() {
        return ItemCameraTransforms.NO_TRANSFORMS;
    }

    @Override
    public @NotNull ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}
