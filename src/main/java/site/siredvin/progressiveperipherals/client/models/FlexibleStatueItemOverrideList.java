package site.siredvin.progressiveperipherals.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.NBTBlock;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.NBTUtils;
import site.siredvin.progressiveperipherals.utils.dao.QuadData;
import site.siredvin.progressiveperipherals.utils.dao.QuadList;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.minecraftforge.client.model.SimpleModelTransform.IDENTITY;

public class FlexibleStatueItemOverrideList extends ItemOverrideList {
    public FlexibleStatueItemOverrideList() {
        super();
    }

    public static class FlexibleStatueItemModel implements IBakedModel {

        public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/stone");
        private static final ResourceLocation DUMMY = new ResourceLocation("dummy_name");

        private @Nullable final QuadList quadList;

        public FlexibleStatueItemModel(@Nullable QuadList quadList) {
            this.quadList = quadList;
        }

        private TextureAtlasSprite getTexture() {
            return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(TEXTURE);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
            FaceBakery bakery = new FaceBakery();

            if (quadList == null)
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.STONE.defaultBlockState()).getQuads(state, side, rand);
            Direction stableSide;
            if (side == null) {
                stableSide = Direction.NORTH;
            } else {
                stableSide = side;
            }

            return quadList.list.stream().map(data -> convert(bakery, data, stableSide)).collect(Collectors.toList());
        }

        protected BakedQuad convert(FaceBakery bakery, QuadData data, @NotNull Direction side) {
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
            ItemTransformVec3f transformVec3f = new ItemTransformVec3f(
                    new Vector3f(45, 45, 45),
                    new Vector3f(),
                    new Vector3f(1f, 1f, 1f)
            );
            return new ItemCameraTransforms(transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f);
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.EMPTY;
        }
    }


    @Override
    public IBakedModel resolve(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
        CompoundNBT internalData = stack.getTagElement(NBTBlock.INTERNAL_DATA_TAG);
        QuadList list = null;
        if (internalData != null && internalData.contains(FlexibleStatueTileEntity.BAKED_QUADS_TAG))
            list = NBTUtils.readQuadList(internalData.getByteArray(FlexibleStatueTileEntity.BAKED_QUADS_TAG));
        return new FlexibleStatueItemModel(list);
    }
}
