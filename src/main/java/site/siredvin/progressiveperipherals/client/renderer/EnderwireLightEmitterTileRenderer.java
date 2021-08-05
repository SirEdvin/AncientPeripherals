package site.siredvin.progressiveperipherals.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireLightEmitterTileEntity;
import site.siredvin.progressiveperipherals.utils.ColorUtils;
import site.siredvin.progressiveperipherals.utils.TERUtils;

import java.awt.*;

public class EnderwireLightEmitterTileRenderer extends TileEntityRenderer<EnderwireLightEmitterTileEntity> {

    private static final float step = 0.0625f;
    private static final float half_step = step / 2;

    public EnderwireLightEmitterTileRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(EnderwireLightEmitterTileEntity entity, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (entity.isEnabled()) {
            Color color = entity.getColor();
            TERUtils.drawBottomlessCube(
                    stack, buffer, ColorUtils.swapAlpha(color, 50), combinedLightIn,
                    new Vector3f(0.25f + half_step, step,  0.25f + half_step),
                    new Vector3f(0.75f - half_step, 0.5f, 0.75f - half_step)
            );
        }
    }
}
