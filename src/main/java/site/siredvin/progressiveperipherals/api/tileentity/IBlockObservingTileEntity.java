package site.siredvin.progressiveperipherals.api.tileentity;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public interface IBlockObservingTileEntity {

    default void placed() {

    }

    default void destroy() {

    }

    default void onNeighbourChange(@Nonnull BlockPos neighbour) {

    }

    default void onNeighbourTileEntityChange(@Nonnull BlockPos neighbour) {

    }

    default void blockTick() {

    }
}
