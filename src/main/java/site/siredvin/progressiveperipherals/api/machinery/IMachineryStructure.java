package site.siredvin.progressiveperipherals.api.machinery;

import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface IMachineryStructure {
    void traverse(Consumer<BlockPos> consumer);
    void traverseInside(Consumer<BlockPos> consumer);
    void traverseFloor(Consumer<BlockPos> consumer);
    BlockPos getCenter();
    void traverseCorners(Consumer<BlockPos> consumer);
    void traverseInsideSides(Consumer<BlockPos> consumer);
    boolean isInside(BlockPos pos);
    boolean isInsideUpOrDownSide(BlockPos pos);
    boolean isBelongTo(BlockPos pos);
}
