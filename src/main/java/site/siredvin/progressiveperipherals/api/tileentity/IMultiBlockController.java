package site.siredvin.progressiveperipherals.api.tileentity;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockStructure;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockPropertiesUtils;

import java.util.Objects;

public interface IMultiBlockController {
    Pair<Boolean, String> detectMultiBlock();
    @Nullable IMultiBlockStructure getStructure();
    @Nullable World getLevel();
    boolean isConfigured();

    void deconstructionCallback();

    default void deconstructMultiBlock() {
        World world = getLevel();
        Objects.requireNonNull(world); // should never happen
        if (!isConfigured())
            return;
        IMultiBlockStructure structure = getStructure();
        if (structure == null)
            return;
        structure.traverseCorners(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            world.setBlockAndUpdate(blockPos, MultiBlockPropertiesUtils.setConnected(state, false));
        });
        structure.traverseInsideSides(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            world.setBlockAndUpdate(blockPos, MultiBlockPropertiesUtils.setConnected(state, false));
        });
        deconstructionCallback();
    }
}
