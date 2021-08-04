package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.core.computer.ComputerSide;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;

public class OrientationUtils {

    protected static ComputerSide toSideHorizontal(Direction facing, Direction offset) {
        if (offset == Direction.UP)
            return ComputerSide.BOTTOM;
        if (offset == Direction.DOWN)
            return ComputerSide.TOP;
        if (facing.equals(offset))
            return ComputerSide.BACK;
        if (facing.equals(offset.getOpposite()))
            return ComputerSide.FRONT;
        if (facing.equals(offset.getClockWise()))
            return ComputerSide.RIGHT;
        return ComputerSide.LEFT;
    }

    protected static ComputerSide toSideWall(Direction facing, Direction offset) {
        switch (offset) {
            case UP:
                return ComputerSide.BACK;
            case DOWN:
                return ComputerSide.FRONT;
        }
        if (facing.equals(offset.getCounterClockWise()))
            return ComputerSide.RIGHT;
        if (facing.equals(offset.getClockWise()))
            return ComputerSide.LEFT;
        if (facing.equals(offset.getOpposite()))
            return ComputerSide.TOP;
        return ComputerSide.BOTTOM;
    }

    public static ComputerSide toSide(Direction facing, AttachFace face, Direction offset) {
        switch (face) {
            case WALL:
                return toSideWall(facing, offset);
            case CEILING:
            case FLOOR:
                return toSideHorizontal(facing, offset);
        }
        return toSideWall(facing, offset);
    }

    protected static Direction toOffsetHorizontal(Direction facing, ComputerSide side) {
        switch (side) {
            case BOTTOM:
                return Direction.UP;
            case TOP:
                return Direction.DOWN;
            case BACK:
                return facing.getOpposite();
            case FRONT:
                return facing;
            case RIGHT:
                return facing.getClockWise();
            case LEFT:
                return facing.getCounterClockWise();
        }
        return facing;
    }

    protected static Direction toOffsetWall(Direction facing, ComputerSide side) {
        switch (side) {
            case BACK:
                return Direction.UP;
            case FRONT:
                return Direction.DOWN;
            case RIGHT:
                return facing.getCounterClockWise();
            case LEFT:
                return facing.getClockWise();
            case TOP:
                return facing.getOpposite();
            case BOTTOM:
                return facing;
        }
        return facing;
    }

    public static Direction toOffset(Direction facing, AttachFace face, ComputerSide side) {
        switch (face) {
            case WALL:
                return toOffsetWall(facing, side);
            case CEILING:
            case FLOOR:
                return toOffsetHorizontal(facing, side);
        }
        return toOffsetWall(facing, side);
    }
}
