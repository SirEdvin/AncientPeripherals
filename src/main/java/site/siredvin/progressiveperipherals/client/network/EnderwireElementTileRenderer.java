package site.siredvin.progressiveperipherals.client.network;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.common.tileentities.network.NetworkConnectorTileEntity;

import java.util.Objects;

public class EnderwireElementTileRenderer extends TileEntityRenderer<NetworkConnectorTileEntity> {

    public EnderwireElementTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(NetworkConnectorTileEntity tileEntity, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        String attachedNetwork = tileEntity.getAttachedNetwork();

        int lightLevel = getLightLevel(Objects.requireNonNull(tileEntity.getLevel()), tileEntity.getBlockPos().above());

        if (attachedNetwork != null) {
            renderLabel(
                    matrixStackIn, bufferIn, lightLevel,
                    new StringTextComponent(String.format("Attached to: %s", attachedNetwork)), 0xffffff
            );
        } else {
            renderLabel(
                    matrixStackIn, bufferIn, lightLevel,
                    new StringTextComponent("Not attached to any network"), 0xff0000
            );
        }
    }

    private void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, ITextComponent text, int color) {

        FontRenderer font = Minecraft.getInstance().font;

        Vector3d translation = new Vector3d(0.5, 1.5, 0.5);

        stack.pushPose();
        float scale = 0.01f;
        int opacity = (int) (.4f * 255.0f) << 24;
        float offset = (float) (-font.width(text) / 2);
        Matrix4f matrix = stack.last().pose();

        stack.translate(translation.x, translation.y, translation.z);
        stack.scale(scale, scale, scale);
        stack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        stack.mulPose(Vector3f.ZP.rotationDegrees(180f));

        font.drawInBatch(text, offset, 0, color, false, matrix, buffer, false, opacity, lightLevel);
        stack.popPose();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getBrightness(LightType.BLOCK, pos);
        int sLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
