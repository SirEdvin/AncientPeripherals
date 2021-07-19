package site.siredvin.progressiveperipherals.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityStackContainer;

public class PedestalTileRenderer<T extends TileEntity & ITileEntityStackContainer> extends TileEntityRenderer<T> {

    public PedestalTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntity, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack storedStack = tileEntity.getStoredStack();
        if (storedStack.isEmpty())
            return;

        ClientPlayerEntity player = Minecraft.getInstance().player;
        int lightLevel = getLightLevel(tileEntity.getLevel(), tileEntity.getBlockPos().above());

        renderItem(storedStack, new double[] { 0.5d, 0.8d, 0.5d }, matrixStackIn, bufferIn, partialTicks,
                combinedOverlayIn, lightLevel, 0.8f);

        renderLabel(matrixStackIn, bufferIn, lightLevel, new double[] { .5d, 1d, .5d }, storedStack.getDisplayName(), 0xffffff);
    }

    private void renderItem(ItemStack stack, double[] translation, MatrixStack matrixStack,
                            IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
        float time = (Minecraft.getInstance().level.getGameTime()+partialTicks)*5;

        matrixStack.pushPose();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(time % 360));
        matrixStack.scale(scale, scale, scale);

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, null, null);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer,
                lightLevel, combinedOverlay, model);
        matrixStack.popPose();
    }

    private void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, double[] corner,
                             ITextComponent text, int color) {

        FontRenderer font = Minecraft.getInstance().font;

        stack.pushPose();
        float scale = 0.01f;
        int opacity = (int) (.4f * 255.0f) << 24;
        float offset = (float) (-font.width(text) / 2);
        Matrix4f matrix = stack.last().pose();

        stack.translate(corner[0], corner[1] + .4f, corner[2]);
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
