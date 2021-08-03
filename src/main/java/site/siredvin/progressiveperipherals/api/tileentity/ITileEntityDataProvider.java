package site.siredvin.progressiveperipherals.api.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

public interface ITileEntityDataProvider {
    CompoundNBT saveInternalData(CompoundNBT data);
    void loadInternalData(BlockState state, CompoundNBT data);
    void pushInternalDataChangeToClient();
    void pushInternalDataChangeToClient(BlockState state);
    void triggerRenderUpdate();
}
