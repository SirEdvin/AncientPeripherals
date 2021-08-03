package site.siredvin.progressiveperipherals.utils;

import dan200.computercraft.core.computer.ComputerSide;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;

public class OrientationUtils {

    protected static ComputerSide toSideFloor(Direction facing, Direction offset) {
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

    protected static ComputerSide toSideCelling(Direction facing, Direction offset) {
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
                return toSideCelling(facing, offset);
            case FLOOR:
                return toSideFloor(facing, offset);
        }
        return toSideWall(facing, offset);
    }

    public static Direction toOffset(Direction facing, ComputerSide side) {
        return Direction.UP;
    }
}
