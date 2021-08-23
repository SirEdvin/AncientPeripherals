package site.siredvin.progressiveperipherals.api.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ITrickedTileEntity<T extends TileEntity & ITrickedTileEntity<T>> extends IWorldObject {
    T getThis();
    default BlockPos getPosition() {
        return getThis().getBlockPos();
    }

    default @Nullable World getWorld() {
        return getThis().getLevel();
    }
}
