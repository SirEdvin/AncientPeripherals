package site.siredvin.progressiveperipherals.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;

import java.awt.*;

public class RealityBreakthroughPointTileRenderer extends TileEntityRenderer<RealityBreakthroughPointTileEntity> {
    public static final ResourceLocation MAIN = new ResourceLocation(ProgressivePeripherals.MOD_ID,"textures/misc/breakthrough_point.png");
    public RealityBreakthroughPointTileRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(RealityBreakthroughPointTileEntity entity, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        stack.pushPose();
        IVertexBuilder vertex = buffer.getBuffer(RenderType.text(MAIN));
        stack.translate(0.5,0.5,0.5);



        Quaternion quaternion = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();

        float time = (Minecraft.getInstance().level.getGameTime()+partialTicks)*5;
        Color color = entity.getColor();

        stack.mulPose(quaternion);
        stack.mulPose(Vector3f.ZP.rotationDegrees(time%360));
        stack.scale(1.5f,1.5f,1.5f);
        Matrix4f matrix = stack.last().pose();
        vertex.vertex(matrix, -0.5f,0.5f,0)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        vertex.vertex(matrix,  0.5f,0.5f,0)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        vertex.vertex(matrix,  0.5f,-0.5f,0)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        vertex.vertex(matrix,  -0.5f,-0.5f,0)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        stack.popPose();

        stack.pushPose();
        stack.translate(0.5,0.5,0.5);
        stack.mulPose(quaternion);
        stack.mulPose(Vector3f.ZN.rotationDegrees(time%360));
        Matrix4f matrix2 = stack.last().pose();
        stack.scale(1.5f,1.5f,1.5f);
        vertex.vertex(matrix2, -0.5f,0.5f,0.001f)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        vertex.vertex(matrix2,  0.5f,0.5f,0.001f)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        vertex.vertex(matrix2,  0.5f,-0.5f,0.001f)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        vertex.vertex(matrix2,  -0.5f,-0.5f,0.001f)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).endVertex();
        stack.popPose();
    }
}
