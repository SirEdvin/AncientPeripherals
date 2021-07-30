package site.siredvin.progressiveperipherals.api.machinery;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.machinery.CubeMachineryStructure;
import site.siredvin.progressiveperipherals.common.machinery.MachineryStructureUtil;
import site.siredvin.progressiveperipherals.utils.RepresentationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ICubeMachineryController<T extends TileEntity & IMachineryController<T>> extends IMachineryController<T> {
    int getSize();
    void setStructure(@Nullable CubeMachineryStructure structure);

    @Override
    default void forgetStructure() {
        setStructure(null);
    }

    default Pair<Boolean, String> detectMultiBlock() {
        World world = getWorld();
        int size = getSize();
        Objects.requireNonNull(world); // should never happen
        if (isConfigured())
            return Pair.onlyLeft(true);
        BlockPos pos = getPosition();
        BlockPos lowestPoint = MachineryStructureUtil.findLowestPoint(world, pos, getCasingPredicate(), size + 1);
        if (lowestPoint == null)
            return Pair.of(false, "Cannot find lowest point ...");
        Pair<BlockPos, BlockPos> corners = MachineryStructureUtil.findCorners(world, lowestPoint, getCornerPredicate(), size + 1);
        if (corners == null)
            return Pair.of(false, "Cannot find lowest corners ...");
        BlockPos northWestCorner = corners.getLeft();
        BlockPos southEastCorner = corners.getRight();
        int calculatedSize = MachineryStructureUtil.calculateSquare(northWestCorner, southEastCorner);
        if (calculatedSize == -1)
            return Pair.of(false, "Floor level is not even square!");
        if (calculatedSize != size)
            return Pair.of(false, String.format("Floor level should be %dx%d square", size, size));
        CubeMachineryStructure structure = new CubeMachineryStructure(northWestCorner, southEastCorner);
        // Check controller position
        if (structure.isInside(pos))
            return Pair.of(false, "Controller shouldn't be inside");
        if (structure.isInsideUpOrDownSide(pos))
            return Pair.of(false, "Controller shouldn't be inside up or down side");
        // Check corners
        List<BlockPos> incorrectPlacedBlocks = new ArrayList<>();
        structure.traverseCorners(blockPos -> {
            BlockState trState = world.getBlockState(blockPos);
            if (!getCornerPredicate().test(trState))
                incorrectPlacedBlocks.add(blockPos);
        });
        if (!incorrectPlacedBlocks.isEmpty())
            return Pair.of(false, String.format("Incorrect placed corners presents: %s", RepresentationUtils.mergeValues(incorrectPlacedBlocks)));
        // Check internals
        structure.traverseInsideSides(blockPos -> {
            BlockState trState = world.getBlockState(blockPos);
            if (!getCasingPredicate().test(trState))
                incorrectPlacedBlocks.add(blockPos);
        });
        if (!incorrectPlacedBlocks.isEmpty())
            return Pair.of(false, String.format("Incorrect placed blocks presents: %s", RepresentationUtils.mergeValues(incorrectPlacedBlocks)));
        // Check is point present
        BlockPos center = structure.getCenter();
        if (!getCenterPredicate().test(world.getBlockState(center)))
            return Pair.of(false, "Incorrect center block");
        structure.traverseInside(blockPos -> {
            if (!blockPos.equals(center) && !getInsidePredicate().test(world.getBlockState(blockPos)))
                incorrectPlacedBlocks.add(blockPos);
        });
        if (!incorrectPlacedBlocks.isEmpty())
            return Pair.of(false, String.format("This blocks should be empty: %s", RepresentationUtils.mergeValues(incorrectPlacedBlocks)));
        structure.setupFacingAndConnections(world, pos);
        setStructure(structure);
        commonDetect();
        return Pair.onlyLeft(true);
    }
}
