package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.ArrayList;
import java.util.List;

public enum UltimineMode {
    NONE(0, 0, 0, 0, 0,0),
    M_3x3(-1, 1, -1, 1, 0, 0),
    M_5x5(-2, 2, -2, 2, 0, 0),
    M_1x2(0, 0, 0, 1, 0, 0),
    M_3x1(-1, 1, 0, 0, 0, 0),
    M_depth_5(0, 0, 0, 0, 0, 5);

    private static final String NONE_NAME = "none";
    private static final String LOCALIZATION_PREFIX = "ultimine_mode";

    private final int startX;
    private final int endX;
    private final int startY;
    private final int endY;
    private final int startZ;
    private final int endZ;

    UltimineMode(int startX, int endX, int startY, int endY, int startZ, int endZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    public @NotNull List<BlockPos> getTargetArea(@NotNull Direction miningDirection, @NotNull Direction turtleFacingDirection, @NotNull BlockPos center) {
        List<BlockPos> result = new ArrayList<>();
        Direction.Axis zAxis = miningDirection.getAxis();
        Direction.AxisDirection zDirection = miningDirection.getAxisDirection();
        Direction.Axis yAxis;
        Direction.Axis xAxis;
        Direction.AxisDirection yDirection;
        Direction.AxisDirection xDirection;
        if (zAxis == Direction.Axis.Y) {
            // this branch requires turtleFacingDirection to understand x and y axis
            yAxis = turtleFacingDirection.getAxis();
            yDirection = turtleFacingDirection.getAxisDirection();
            xDirection = Direction.AxisDirection.POSITIVE;
            if (yAxis == Direction.Axis.Z) {
                xAxis = Direction.Axis.X;
            } else {
                xAxis = Direction.Axis.Z;
            }
        } else {
            yAxis = Direction.Axis.Y;
            yDirection = Direction.AxisDirection.POSITIVE;
            xDirection = Direction.AxisDirection.POSITIVE;
            if (zAxis == Direction.Axis.X) {
                xAxis = Direction.Axis.Z;
            } else {
                xAxis = Direction.Axis.X;
            }
        }
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    result.add(
                            center.relative(xAxis, xDirection == Direction.AxisDirection.POSITIVE? x: -x)
                            .relative(yAxis, yDirection == Direction.AxisDirection.POSITIVE? y: -y)
                            .relative(zAxis, zDirection == Direction.AxisDirection.POSITIVE? z: -z)
                    );
                }
            }
        }
        return result;
    }

    public String prettyName() {
        if (this == UltimineMode.NONE) {
            return NONE_NAME;
        }
        return name().substring(2);
    }

    public String getDescription() {
        return TranslationUtil.localization(LOCALIZATION_PREFIX, prettyName()).getString();
    }

    public static UltimineMode fromPretty(String prettyName) {
        if (prettyName.equals(NONE_NAME))
            return NONE;
        return UltimineMode.valueOf("M_" + prettyName);
    }
}
