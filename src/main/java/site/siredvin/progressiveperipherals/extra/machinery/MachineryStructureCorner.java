package site.siredvin.progressiveperipherals.extra.machinery;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class MachineryStructureCorner {
    private final Direction zDirection;
    private final Direction xDirection;
    private final Direction yDirection;
    private final BlockPos pos;

    private MachineryStructureCorner(Direction xDirection, Direction yDirection, Direction zDirection, BlockPos pos) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
        this.zDirection = zDirection;
        this.pos = pos;
    }

    public Direction getXDirection() {
        return xDirection;
    }

    public Direction getYDirection() {
        return yDirection;
    }

    public Direction getZDirection() {
        return zDirection;
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public int getZ() {
        return pos.getZ();
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockPos offsetX(int offset) {
        return pos.relative(xDirection, offset);
    }

    public BlockPos offsetY(int offset) {
        return pos.relative(yDirection, offset);
    }

    public BlockPos offsetZ(int offset) {
        return pos.relative(zDirection, offset);
    }

    public BlockPos offsetXZ(int x, int z) {
        return pos.relative(xDirection, x).relative(zDirection, z);
    }

    public BlockPos offsetYZ(int y, int z) {
        return pos.relative(yDirection, y).relative(zDirection, z);
    }

    public BlockPos offsetXY(int x, int y) {
        return pos.relative(xDirection, x).relative(yDirection, y);
    }

    public BlockPos offset(int x, int y, int z) {
        return pos.relative(xDirection, x).relative(yDirection, y).relative(zDirection, z);
    }

    public MachineryStructureCorner opposite(int offset) {
        return new MachineryStructureCorner(
                xDirection.getOpposite(), yDirection.getOpposite(), zDirection.getOpposite(),
                offset(offset, offset, offset)
        );
    }

    public static MachineryStructureCorner northWestLowest(BlockPos pos) {
        return new MachineryStructureCorner(Direction.EAST, Direction.UP, Direction.SOUTH, pos);
    }

    public static MachineryStructureCorner southEastLowest(BlockPos pos) {
        return new MachineryStructureCorner(Direction.WEST, Direction.UP, Direction.NORTH, pos);
    }

    public static MachineryStructureCorner northEastUpper(BlockPos pos) {
        return new MachineryStructureCorner(Direction.WEST, Direction.DOWN, Direction.SOUTH, pos);
    }

    public static MachineryStructureCorner southWestUpper(BlockPos pos) {
        return new MachineryStructureCorner(Direction.EAST, Direction.DOWN, Direction.NORTH, pos);
    }

}
