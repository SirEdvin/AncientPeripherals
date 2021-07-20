package site.siredvin.progressiveperipherals.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityStackContainer;
import site.siredvin.progressiveperipherals.common.blocks.base.BasePedestal;

import java.util.Objects;

public class PedestalTileRenderer<T extends TileEntity & ITileEntityStackContainer> extends TileEntityRenderer<T> {

    private static final Vector3d ITEM_TRANSLATE_UP = new Vector3d(0.5, 0.8, 0.5);
    public static final Vector3d LABEL_TRANSLATE_UP = new Vector3d(0.5, 1.2, 0.5);

    private static final Vector3d ITEM_TRANSLATE_DOWN = new Vector3d(0.5, 0.2, 0.5);
    private static final Vector3d LABEL_TRANSLATE_DOWN = new Vector3d(0.5, -0.1, 0.5);

    private static final Vector3d ITEM_TRANSLATE_NORTH = new Vector3d(0.5, 0.5, 0.1);
    private static final Vector3d LABEL_TRANSLATE_NORTH = new Vector3d(0.5, 1, 0.5);

    private static final Vector3d ITEM_TRANSLATE_SOUTH = new Vector3d(0.5, 0.5, 0.9);
    private static final Vector3d LABEL_TRANSLATE_SOUTH = new Vector3d(0.5, 1, 0.5);

    private static final Vector3d ITEM_TRANSLATE_EAST = new Vector3d(0.9, 0.5, 0.5);
    private static final Vector3d LABEL_TRANSLATE_EAST = new Vector3d(0.5, 1, 0.5);

    private static final Vector3d ITEM_TRANSLATE_WEST = new Vector3d(0, 0.5, 0.5);
    private static final Vector3d LABEL_TRANSLATE_WEST = new Vector3d(0.5, 1, 0.5);


    public PedestalTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public Vector3d getItemTranslate(Direction direction) {
        switch (direction) {
            case DOWN:
                return ITEM_TRANSLATE_DOWN;
            case NORTH:
                return ITEM_TRANSLATE_NORTH;
            case SOUTH:
                return ITEM_TRANSLATE_SOUTH;
            case EAST:
                return ITEM_TRANSLATE_EAST;
            case WEST:
                return ITEM_TRANSLATE_WEST;
        }
        return ITEM_TRANSLATE_UP;
    }

    public Vector3d getLabelTranslate(Direction direction) {
        switch (direction) {
            case DOWN:
                return LABEL_TRANSLATE_DOWN;
            case NORTH:
                return LABEL_TRANSLATE_NORTH;
            case SOUTH:
                return LABEL_TRANSLATE_SOUTH;
            case EAST:
                return LABEL_TRANSLATE_EAST;
            case WEST:
                return LABEL_TRANSLATE_WEST;
        }
        return LABEL_TRANSLATE_UP;
    }

    public @Nullable Quaternion itemRotation(Direction direction) {
        switch (direction) {
            case NORTH:
                return Vector3f.YP.rotationDegrees(90);
            case SOUTH:
                return Vector3f.YP.rotationDegrees(270);
            case WEST:
                return Vector3f.ZP.rotationDegrees(90);
            case DOWN:
                return Vector3f.XP.rotationDegrees(180);
        }
        return null;
    }

    public Quaternion itemTimeRotation(Direction direction, float time) {
        switch (direction) {
            case WEST:
                return Vector3f.XP.rotationDegrees(time % 360);
        }
        return Vector3f.YP.rotationDegrees(time % 360);
    }

    @Override
    public void render(T tileEntity, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack storedStack = tileEntity.getStoredStack();
        if (storedStack.isEmpty())
            return;

        Direction blockDirection = tileEntity.getBlockState().getValue(BasePedestal.FACING);

        int lightLevel = getLightLevel(Objects.requireNonNull(tileEntity.getLevel()), tileEntity.getBlockPos().above());

        renderItem(storedStack, blockDirection, matrixStackIn, bufferIn, partialTicks, combinedOverlayIn, lightLevel, 0.8f);

        renderLabel(matrixStackIn, bufferIn, lightLevel, getLabelTranslate(blockDirection), storedStack.getDisplayName(), 0xffffff);
    }

    private void renderItem(ItemStack stack, Direction direction, MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
        float time = (Minecraft.getInstance().level.getGameTime() + partialTicks) * 5;
        Vector3d translation = getItemTranslate(direction);
        Quaternion itemRotation = itemRotation(direction);

        matrixStack.pushPose();
        matrixStack.translate(translation.x, translation.y, translation.z);
        if (itemRotation != null)
            matrixStack.mulPose(itemRotation);
        matrixStack.mulPose(itemTimeRotation(direction, time));
        matrixStack.scale(scale, scale, scale);

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, null, null);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer,
                lightLevel, combinedOverlay, model);
        matrixStack.popPose();
    }

    private void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, Vector3d translation, ITextComponent text, int color) {

        FontRenderer font = Minecraft.getInstance().font;

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
