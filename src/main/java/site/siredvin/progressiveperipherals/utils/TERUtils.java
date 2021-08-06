package site.siredvin.progressiveperipherals.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TERUtils {
    private static final ResourceLocation MAIN = new ResourceLocation(ProgressivePeripherals.MOD_ID,"textures/misc/full_white.png");

    /**
     * Return leftToRightDirection and bottomToTopDirection vectors
     */
    private static Pair<Vector3f, Vector3f> getDirectionVertex(Direction face) {
        switch (face) {
            case NORTH: { // bottom left is east
                return Pair.of(Vector3f.XN, Vector3f.YP);
            }
            case SOUTH: {  // bottom left is west
                return Pair.of(Vector3f.XP, Vector3f.YP);
            }
            case EAST: {  // bottom left is south
                return Pair.of(Vector3f.ZN, Vector3f.YP);
            }
            case WEST: { // bottom left is north
                return Pair.of(Vector3f.ZP, Vector3f.YP);
            }
            case UP: { // bottom left is southwest by minecraft block convention
                return Pair.of(Vector3f.XN, Vector3f.ZP);
            }
            case DOWN: { // bottom left is northwest by minecraft block convention
                return Pair.of(Vector3f.XP, Vector3f.ZP);
            }
        }
        return Pair.of(Vector3f.XN, Vector3f.YP);
    }


    private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                                      Vector3f pos, Vector2f texUV,
                                      Vector3f normalVector, Color color, int lightmapValue) {
        renderBuffer.vertex(matrixPos, pos.x(), pos.y(), pos.z())
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(texUV.x, texUV.y)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(lightmapValue)
                .normal(matrixNormal, normalVector.x(), normalVector.y(), normalVector.z())
                .endVertex();
    }

    public static void renderRectangle(Direction face, Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer, Color color, Vector3f start, Vector3f end, int lightmapValue) {
        Vector3f bottomLeftPos, bottomRightPos, topRightPos, topLeftPos;
        switch (face) {
            case NORTH:
                bottomLeftPos = new Vector3f(start.x(), start.y(), start.z());
                bottomRightPos = new Vector3f(end.x(), start.y(), start.z());
                topRightPos = new Vector3f(end.x(), end.y(), start.z());
                topLeftPos = new Vector3f(start.x(), end.y(), start.z());
                break;
            case SOUTH:
                bottomLeftPos = new Vector3f(start.x(), start.y(), end.z());
                bottomRightPos = new Vector3f(end.x(), start.y(), end.z());
                topRightPos = new Vector3f(end.x(), end.y(), end.z());
                topLeftPos = new Vector3f(start.x(), end.y(), end.z());
                break;
            case WEST:
                bottomLeftPos = new Vector3f(start.x(), start.y(), start.z());
                bottomRightPos = new Vector3f(start.x(), start.y(), end.z());
                topRightPos = new Vector3f(start.x(), end.y(), end.z());
                topLeftPos = new Vector3f(start.x(), end.y(), start.z());
                break;
            case EAST:
                bottomLeftPos = new Vector3f(end.x(), start.y(), start.z());
                bottomRightPos = new Vector3f(end.x(), start.y(), end.z());
                topRightPos = new Vector3f(end.x(), end.y(), end.z());
                topLeftPos = new Vector3f(end.x(), end.y(), start.z());
                break;
            case UP:
                bottomLeftPos = new Vector3f(start.x(), end.y(), start.z());
                bottomRightPos = new Vector3f(start.x(), end.y(), end.z());
                topRightPos = new Vector3f(end.x(), end.y(), end.z());
                topLeftPos = new Vector3f(end.x(), end.y(), start.z());
                break;
            default: // same as DOWN
                bottomLeftPos = new Vector3f(start.x(), start.y(), start.z());
                bottomRightPos = new Vector3f(start.x(), start.y(), end.z());
                topRightPos = new Vector3f(end.x(), start.y(), end.z());
                topLeftPos = new Vector3f(end.x(), start.y(), start.z());
                break;
        }
        Vector2f uvpos = new Vector2f(0, 0);
        Vector3f normalVector = face.step();  // gives us the normal to the face

        addVertex(matrixPos, matrixNormal, renderBuffer, bottomLeftPos, uvpos, normalVector, color, lightmapValue);
        addVertex(matrixPos, matrixNormal, renderBuffer, bottomRightPos, uvpos, normalVector, color, lightmapValue);
        addVertex(matrixPos, matrixNormal, renderBuffer, topRightPos, uvpos, normalVector, color, lightmapValue);
        addVertex(matrixPos, matrixNormal, renderBuffer, topLeftPos, uvpos, normalVector, color, lightmapValue);
    }

    public static void drawCubeWithoutSide(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, Color color, int combinedLight, Vector3f start, Vector3f end, Direction side) {
        List<Direction> sides = new ArrayList<>(Arrays.asList(Direction.values()));
        sides.remove(side);
        drawCube(matrixStack, renderBuffer, color, combinedLight, start, end, sides);
    }

    public static void drawCube(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, Color color, int combinedLight, Vector3f start, Vector3f end) {
        drawCube(matrixStack, renderBuffer, color, combinedLight, start, end, Arrays.asList(Direction.values()));
    }

    public static void drawCube(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, Color color, int combinedLight, Vector3f start, Vector3f end, List<Direction> sides) {
        IVertexBuilder vertexBuilder = renderBuffer.getBuffer(RenderType.entityTranslucent(MAIN));
        Matrix4f matrixPos = matrixStack.last().pose();
        Matrix3f matrixNormal = matrixStack.last().normal();
        for (Direction side: sides)
            renderRectangle(side, matrixPos, matrixNormal, vertexBuilder, color, start, end, combinedLight);
    }
}
