package site.siredvin.progressiveperipherals.api.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

public interface ITileEntityDataProvider {
    CompoundNBT saveInternalData(CompoundNBT data);
    void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate);
}