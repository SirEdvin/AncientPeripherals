package site.siredvin.progressiveperipherals.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.client.models.abstr.AbstractFlexibleStatueModel;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseNBTBlock;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.NBTUtils;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.util.List;
import java.util.Random;

public class FlexibleStatueItemOverrideList extends ItemOverrideList {
    public FlexibleStatueItemOverrideList() {
        super();
    }

    public static class FlexibleStatueItemModel extends AbstractFlexibleStatueModel {

        private @Nullable final QuadList quadList;

        public FlexibleStatueItemModel(@Nullable QuadList quadList) {
            this.quadList = quadList;
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand) {
            return getQuads(state, side, rand, quadList);
        }

        @Override
        public @NotNull ItemCameraTransforms getTransforms() {
            ItemTransformVec3f transformVec3f = new ItemTransformVec3f(
                    new Vector3f(30, 225, 0),
                    new Vector3f(),
                    new Vector3f(0.625f, 0.625f, 0.625f)
            );
            return new ItemCameraTransforms(transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f, transformVec3f);
        }
    }


    @Override
    public IBakedModel resolve(@NotNull IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
        CompoundNBT internalData = stack.getTagElement(BaseNBTBlock.INTERNAL_DATA_TAG);
        QuadList list = null;
        if (internalData != null && internalData.contains(FlexibleStatueTileEntity.BAKED_QUADS_TAG)) {
            list = NBTUtils.readQuadList(internalData.getByteArray(FlexibleStatueTileEntity.BAKED_QUADS_TAG));
            if (list == null) { // this happends only on errors data and required cleanup
                internalData.remove(FlexibleStatueTileEntity.BAKED_QUADS_TAG);
            }
        }
        return new FlexibleStatueItemModel(list);
    }
}
