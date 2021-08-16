package site.siredvin.progressiveperipherals.extra.network.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEnderwireNetworkElement {
    @NotNull BlockPos getPos();
    @NotNull String getName();
    @NotNull EnderwireElementCategory getCategory();
    @NotNull EnderwireElementType getElementType();
    @NotNull NetworkAmplifier getNetworkAmplifier();
    @NotNull String getDimension();
    @NotNull CompoundNBT toNBT();
    @NotNull String getTypeMark();
    @Nullable IEnderwireElement getElement(World world);

    /**
     * @return is enderwire network element can be considered as stable and will be stored in general network data
     */
    default boolean isStable() {
        return true;
    }
}
