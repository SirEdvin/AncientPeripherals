package site.siredvin.progressiveperipherals.common.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SquareMultiBlock implements IMultiBlockStructure {
    private final int size;
    private final MultiBlockCorner northWestLowest;

    public SquareMultiBlock(BlockPos northWestLowest, int size) {
        this.size = size;
        this.northWestLowest = MultiBlockCorner.northWestLowest(northWestLowest);
    }

    public SquareMultiBlock(BlockPos northWestLowest, BlockPos southEastCorner) {
        this.size = MultiBlockUtils.calculateSquare(northWestLowest, southEastCorner);
        if (size == -1)
            throw new IllegalArgumentException("Coordinate should create square with yDiff 0");
        this.northWestLowest = MultiBlockCorner.northWestLowest(northWestLowest);
    }

    public int getSize() {
        return size;
    }

    public MultiBlockCorner getNorthWestLowest() {
        return northWestLowest;
    }

    public void traverse(Consumer<BlockPos> consumer) {
        for (int xOffset = 0; xOffset < size; xOffset++)
            for (int zOffset = 0; zOffset < size; zOffset++)
                for (int yOffset = 0; yOffset < size; yOffset++)
                    consumer.accept(northWestLowest.offset(xOffset, yOffset, zOffset));
    }

    public void traverseInside(Consumer<BlockPos> consumer) {
        for (int xOffset = 1; xOffset < size - 1; xOffset++)
            for (int zOffset = 1; zOffset < size - 1; zOffset++)
                for (int yOffset = 1; yOffset < size - 1; yOffset++)
                    consumer.accept(northWestLowest.offset(xOffset, yOffset, zOffset));
    }

    public void traverseFloor(Consumer<BlockPos> consumer) {
        for (int xOffset = 0; xOffset < size; xOffset++)
            for (int zOffset = 0; zOffset < size; zOffset++)
                consumer.accept(northWestLowest.offsetXZ(xOffset, zOffset));
    }

    public List<MultiBlockCorner> getMainCorners() {
        return new ArrayList<MultiBlockCorner>() {{
            add(northWestLowest);
            add(MultiBlockCorner.southEastLowest(northWestLowest.offsetXZ(size - 1, size - 1)));
            add(MultiBlockCorner.northEastUpper(northWestLowest.offsetXY(size - 1, size - 1)));
            add(MultiBlockCorner.southWestUpper(northWestLowest.offsetYZ(size - 1, size - 1)));
        }};
    }

    public BlockPos getCenter() {
        int middlePoint = size / 2;
        return northWestLowest.offset(middlePoint, middlePoint, middlePoint);
    }

    public void traverseCorners(Consumer<BlockPos> consumer) {
        List<MultiBlockCorner> mainCorners = getMainCorners();
        for (MultiBlockCorner corner: mainCorners) {
            consumer.accept(corner.getPos());
            int limit = size;
            if (corner.getYDirection() == Direction.DOWN) // Trick to avoid duplicate on corners
                limit--;
            for (int offset = 1; offset < limit; offset++) {
                consumer.accept(corner.offsetX(offset));
                consumer.accept(corner.offsetY(offset));
                consumer.accept(corner.offsetZ(offset));
            }
        }
    }

    public void traverseInsideSides(Consumer<BlockPos> consumer) {
        MultiBlockCorner opposite = northWestLowest.opposite(size - 1);
        for (int i = 1; i < size - 1; i++ )
            for (int j = 1; j < size - 1; j++) {
                consumer.accept(northWestLowest.offsetXY(i, j));
                consumer.accept(northWestLowest.offsetYZ(i, j));
                consumer.accept(northWestLowest.offsetXZ(i, j));
                consumer.accept(opposite.offsetXY(i, j));
                consumer.accept(opposite.offsetYZ(i, j));
                consumer.accept(opposite.offsetXZ(i, j));
            }
    }

    public boolean isInside(BlockPos pos) {
        int xDiff = pos.getX() - northWestLowest.getPos().getX();
        int yDiff = pos.getY() - northWestLowest.getPos().getY();
        int zDiff = pos.getZ() - northWestLowest.getPos().getZ();
        return xDiff > 0 && yDiff > 0 && zDiff > 0 && xDiff < size - 1 && yDiff < size - 1 && zDiff < size - 1;
    }

    public boolean isInsideUpOrDownSide(BlockPos pos) {
        int xDiff = pos.getX() - northWestLowest.getPos().getX();
        int yDiff = pos.getY() - northWestLowest.getPos().getY();
        int zDiff = pos.getZ() - northWestLowest.getPos().getZ();
        if (yDiff != 0 && yDiff != size - 1)
            return false;
        return xDiff > 0 && zDiff > 0 && xDiff < size - 1 && zDiff < size - 1;
    }

    private Direction relativeNorthDirection(BlockPos facingReference) {
        int xDiff = facingReference.getX() - northWestLowest.getPos().getX();
        int yDiff = facingReference.getY() - northWestLowest.getPos().getY();
        int zDiff = facingReference.getZ() - northWestLowest.getPos().getZ();
        if (zDiff == 0)
            return Direction.NORTH;
        if (zDiff == size - 1)
            return Direction.SOUTH;
        if (xDiff == 0)
            return Direction.WEST;
        if (xDiff == size - 1)
            return Direction.EAST;
        if (yDiff == 0)
            return Direction.DOWN;
        if (yDiff == size - 1)
            return Direction.UP;
        return Direction.NORTH;
    }

    private Direction calculateFacing(BlockPos pos, BlockPos facingReference) {
        Direction relativeNorth = relativeNorthDirection(facingReference);
        Direction relativeWest = relativeNorth.getCounterClockWise();
        int xDiff = pos.getX() - northWestLowest.getX();
        int yDiff = pos.getY() - northWestLowest.getY();
        int zDiff = pos.getZ() - northWestLowest.getZ();
        Vector3i diffVector = new Vector3i(xDiff, yDiff, zDiff);
        Vector3i relatedVector = relativeNorth.getNormal();
        Vector3i product = new Vector3i(diffVector.getX() * relatedVector.getX(), diffVector.getY() * relatedVector.getY(), diffVector.getZ() * relatedVector.getZ());
        int productScalar = Math.abs(product.getX() + product.getY() + product.getZ());
        if (productScalar == 0)
            return relativeNorth;
        if (productScalar == size - 1)
            return relativeNorth.getOpposite();
        relatedVector = relativeWest.getNormal();
        product = new Vector3i(diffVector.getX() * relatedVector.getX(), diffVector.getY() * relatedVector.getY(), diffVector.getZ() * relatedVector.getZ());
        productScalar = Math.abs(product.getX() + product.getY() + product.getZ());
        if (productScalar == 0)
            return relativeWest;
        if (productScalar == size - 1)
            return relativeWest.getOpposite();
        if (pos.getY() == northWestLowest.getPos().getY())
            return Direction.DOWN;
        return Direction.UP;
    }

    protected void setupFacingAndConnection(World world, BlockPos facingReference, BlockPos exactBlock) {
        world.setBlockAndUpdate(exactBlock, MultiBlockPropertiesUtils.setConnected(MultiBlockPropertiesUtils.setFacing(world.getBlockState(exactBlock), calculateFacing(exactBlock, facingReference)), true));
    }

    public void setupCornersFacingAndConnections(World world, BlockPos facingReference) {
        setupFacingAndConnection(world, facingReference, northWestLowest.getPos());
        setupFacingAndConnection(world, facingReference, northWestLowest.offsetX(size - 1));
        setupFacingAndConnection(world, facingReference, northWestLowest.offsetY(size - 1));
        setupFacingAndConnection(world, facingReference, northWestLowest.offsetXY(size - 1, size - 1));
        setupFacingAndConnection(world, facingReference, northWestLowest.offsetZ(size - 1));
        setupFacingAndConnection(world, facingReference, northWestLowest.offsetXZ(size - 1, size - 1));
        setupFacingAndConnection(world, facingReference, northWestLowest.offsetYZ(size - 1, size - 1));
        setupFacingAndConnection(world, facingReference, northWestLowest.offset(size - 1, size - 1, size - 1));

        for (int offset = 1; offset < size - 1; offset++) {
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetY(offset));
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetXY(size -1, offset));
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetYZ(offset, size - 1));
            setupFacingAndConnection(world, facingReference, northWestLowest.offset(size - 1, offset, size - 1));

            setupFacingAndConnection(world, facingReference, northWestLowest.offsetX(offset));
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetXY(offset, size - 1));
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetXZ(offset, size - 1));
            setupFacingAndConnection(world, facingReference, northWestLowest.offset(offset, size - 1, size - 1));

            setupFacingAndConnection(world, facingReference, northWestLowest.offsetZ(offset));
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetYZ(size - 1, offset));
            setupFacingAndConnection(world, facingReference, northWestLowest.offsetXZ(size - 1, offset));
            setupFacingAndConnection(world, facingReference, northWestLowest.offset(size - 1, size - 1, offset));

        }
    }

    public void setupInsideSidesFacingAndConnections(World world, BlockPos facingReference) {
        traverseInsideSides(blockPos -> setupFacingAndConnection(world, facingReference, blockPos));
    }

    public void setupFacingAndConnections(World world, BlockPos facingReference) {
        setupCornersFacingAndConnections(world, facingReference);
        setupInsideSidesFacingAndConnections(world, facingReference);
    }
}
