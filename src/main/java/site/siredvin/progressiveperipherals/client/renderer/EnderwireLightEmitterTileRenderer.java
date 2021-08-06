package site.siredvin.progressiveperipherals.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireLightEmitterBlock;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireLightEmitterTileEntity;
import site.siredvin.progressiveperipherals.utils.ColorUtils;
import site.siredvin.progressiveperipherals.utils.TERUtils;

import java.awt.Color;

public class EnderwireLightEmitterTileRenderer extends TileEntityRenderer<EnderwireLightEmitterTileEntity> {

    public static final int REQUIRED_COLOR_ALPHA = 50;

    private static final float step = 0.0625f;
    private static final float half_step = step / 2;

    private static final Vector3f FLOOR_START = new Vector3f(0.25f + half_step, step,  0.25f + half_step);
    private static final Vector3f FLOOR_END = new Vector3f(0.75f - half_step, 0.5f, 0.75f - half_step);

    private static final Vector3f CELLING_START = new Vector3f(0.25f + half_step, 0.5f,  0.25f + half_step);
    private static final Vector3f CELLING_END = new Vector3f(0.75f - half_step, 1 - step, 0.75f - half_step);

    private static final Vector3f NORTH_START = new Vector3f(0.25f + half_step, 0.25f + half_step, 0.5f);
    private static final Vector3f NORTH_END = new Vector3f(0.75f - half_step, 0.75f - half_step, 1 - step);

    private static final Vector3f SOUTH_START = new Vector3f(0.25f + half_step, 0.25f + half_step, step);
    private static final Vector3f SOUTH_END = new Vector3f(0.75f - half_step, 0.75f - half_step, 0.5f);

    private static final Vector3f WEST_START = new Vector3f(0.5f, 0.25f + half_step, 0.25f + half_step);
    private static final Vector3f WEST_END = new Vector3f(1 - step, 0.75f - half_step, 0.75f - half_step);

    private static final Vector3f EAST_START = new Vector3f(step, 0.25f + half_step, 0.25f + half_step);
    private static final Vector3f EAST_END = new Vector3f(0.5f, 0.75f - half_step, 0.75f - half_step);

    public EnderwireLightEmitterTileRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(EnderwireLightEmitterTileEntity entity, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (entity.isEnabled()) {
            Color color = entity.getColor();
            BlockState state = entity.getBlockState();
            AttachFace face = state.getValue(EnderwireLightEmitterBlock.FACE);
            Direction facing = state.getValue(EnderwireLightEmitterBlock.FACING);
            switch (face) {
                case WALL:
                    switch (facing) {
                        case NORTH:
                            TERUtils.drawCubeWithoutSide(
                                    stack, buffer, color, combinedLightIn,
                                    NORTH_START,
                                    NORTH_END,
                                    Direction.SOUTH
                            );
                            break;
                        case SOUTH:
                            TERUtils.drawCubeWithoutSide(
                                    stack, buffer, color, combinedLightIn,
                                    SOUTH_START,
                                    SOUTH_END,
                                    Direction.NORTH
                            );
                            break;
                        case WEST:
                            TERUtils.drawCubeWithoutSide(
                                    stack, buffer, color, combinedLightIn,
                                    WEST_START,
                                    WEST_END,
                                    Direction.EAST
                            );
                            break;
                        case EAST:
                            TERUtils.drawCubeWithoutSide(
                                    stack, buffer, color, combinedLightIn,
                                    EAST_START,
                                    EAST_END,
                                    Direction.WEST
                            );
                            break;
                    }
                    break;
                case CEILING:
                    TERUtils.drawCubeWithoutSide(
                            stack, buffer, ColorUtils.swapAlpha(color, 50), combinedLightIn,
                            CELLING_START,
                            CELLING_END,
                            Direction.UP
                    );
                    break;
                case FLOOR:
                    TERUtils.drawCubeWithoutSide(
                            stack, buffer, ColorUtils.swapAlpha(color, 50), combinedLightIn,
                            FLOOR_START,
                            FLOOR_END,
                            Direction.DOWN
                    );
                    break;

            }
        }
    }
}
