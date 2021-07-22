package site.siredvin.progressiveperipherals.api.tileentity;

import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockStructure;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockPropertiesUtils;

import java.util.Objects;
import java.util.function.Predicate;

public interface IMultiBlockController {
    Pair<Boolean, String> detectMultiBlock();
    @Nullable IMultiBlockStructure getStructure();
    @Nullable World getLevel();
    boolean isConfigured();
    void deconstructionCallback();
    Predicate<BlockState> getCasingPredicate();
    Predicate<BlockState> getCornerPredicate();

    default void deconstructMultiBlock() {
        World world = getLevel();
        Objects.requireNonNull(world); // should never happen
        if (!isConfigured())
            return;
        IMultiBlockStructure structure = getStructure();
        if (structure == null)
            return;
        Predicate<BlockState> casingPredicate = getCasingPredicate();
        Predicate<BlockState> cornerPredicate = getCornerPredicate();
        structure.traverseCorners(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (cornerPredicate.test(state))
                world.setBlockAndUpdate(blockPos, MultiBlockPropertiesUtils.setConnected(state, false));
        });
        structure.traverseInsideSides(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (casingPredicate.test(state))
                world.setBlockAndUpdate(blockPos, MultiBlockPropertiesUtils.setConnected(state, false));
        });
        deconstructionCallback();
    }
}
