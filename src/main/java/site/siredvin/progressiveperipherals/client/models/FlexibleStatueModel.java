package site.siredvin.progressiveperipherals.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.dao.QuadData;
import site.siredvin.progressiveperipherals.utils.dao.QuadList;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.minecraftforge.client.model.SimpleModelTransform.IDENTITY;

public class FlexibleStatueModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/stone");
    private static final ResourceLocation DUMMY = new ResourceLocation("dummy_name");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(TEXTURE);
    }


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        QuadList quadsData = extraData.getData(FlexibleStatueTileEntity.BAKED_QUADS);
        FaceBakery bakery = new FaceBakery();

        if (quadsData == null)
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.STONE.defaultBlockState()).getQuads(state, side, rand, extraData);
        Direction stableSide;
        if (side == null) {
            stableSide = Direction.NORTH;
        } else {
            stableSide = side;
        }

        return quadsData.list.stream().map(data -> convert(bakery, data, stableSide)).collect(Collectors.toList());
    }

    protected BakedQuad convert(FaceBakery bakery, QuadData data, @Nonnull Direction side) {
        BlockPartFace face = new BlockPartFace(
                null, -1, "", new BlockFaceUV(data.getUV(), 0)
        );
        return bakery.bakeQuad(
                data.getStart(), data.getEnd(), face, getTexture(), side,
                IDENTITY, null, true, DUMMY
        );
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
    public TextureAtlasSprite getParticleIcon() {
        return getTexture();
    }

    @Override
    public ItemCameraTransforms getTransforms() {
        return ItemCameraTransforms.NO_TRANSFORMS;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new FlexibleStatueItemOverrideList();
    }
}
