package site.siredvin.progressiveperipherals.common.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class MultiBlockCorner {
    private final Direction zDirection;
    private final Direction xDirection;
    private final Direction yDirection;
    private final BlockPos pos;

    private MultiBlockCorner(Direction xDirection, Direction yDirection, Direction zDirection, BlockPos pos) {
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

    public MultiBlockCorner opposite(int offset) {
        return new MultiBlockCorner(
                xDirection.getOpposite(), yDirection.getOpposite(), zDirection.getOpposite(),
                offset(offset, offset, offset)
        );
    }

    public static MultiBlockCorner northWestLowest(BlockPos pos) {
        return new MultiBlockCorner(Direction.EAST, Direction.UP, Direction.SOUTH, pos);
    }

    public static MultiBlockCorner southEastLowest(BlockPos pos) {
        return new MultiBlockCorner(Direction.WEST, Direction.UP, Direction.NORTH, pos);
    }

    public static MultiBlockCorner northEastUpper(BlockPos pos) {
        return new MultiBlockCorner(Direction.WEST, Direction.DOWN, Direction.SOUTH, pos);
    }

    public static MultiBlockCorner southWestUpper(BlockPos pos) {
        return new MultiBlockCorner(Direction.EAST, Direction.DOWN, Direction.NORTH, pos);
    }

}
