package site.siredvin.progressiveperipherals.api.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface IWorldObject {
    BlockPos getPosition();
    @Nullable World getWorld();
}
