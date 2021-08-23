package site.siredvin.progressiveperipherals.client.models.abstr;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.utils.quad.QuadData;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.minecraftforge.client.model.SimpleModelTransform.IDENTITY;

public abstract class AbstractFlexibleStatueModel implements IBakedModel {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ProgressivePeripherals.MOD_ID, "block/flexible_statue_empty");
    private static final ResourceLocation DUMMY = new ResourceLocation("dummy_name");

    @SuppressWarnings("deprecation")
    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(TEXTURE);
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @Nullable  QuadList quadsData) {
        FaceBakery bakery = new FaceBakery();

        if (quadsData == null)
            //noinspection deprecation
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.FLEXIBLE_STATUE.get().defaultBlockState()).getQuads(state, side, rand);
        Direction stableSide;
        if (side == null) {
            stableSide = Direction.NORTH;
        } else {
            stableSide = side;
        }

        return quadsData.list.stream().map(data -> convert(bakery, data, stableSide)).collect(Collectors.toList());
    }

    protected BakedQuad convert(FaceBakery bakery, QuadData data, @NotNull Direction side) {
        BlockPartFace face = new BlockPartFace(
                null, -1, "", new BlockFaceUV(data.getUV(), 0)
        );
        BakedQuad quad = bakery.bakeQuad(
                data.getStart(), data.getEnd(), face, getTexture(), side,
                IDENTITY, null, true, DUMMY
        );
        quad.getVertices()[3] = data.getColor();
        quad.getVertices()[11] = data.getColor();
        quad.getVertices()[19] = data.getColor();
        quad.getVertices()[27] = data.getColor();
        return quad;
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

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemCameraTransforms getTransforms() {
        return ItemCameraTransforms.NO_TRANSFORMS;
    }

    @Override
    public @NotNull ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}
