package site.siredvin.progressiveperipherals.common.tileentities.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;

public abstract class MutableNBTTileEntity extends TileEntity implements ITileEntityDataProvider {
    public MutableNBTTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag = saveInternalData(tag);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        loadInternalData(getBlockState(), tag, false);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadInternalData(state, tag, true);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = saveInternalData(tag);
        return super.save(tag);
    }

    public void pushState() {
        pushState(getBlockState());
    }

    public void pushState(BlockState state) {
        MutableNBTTileEntityEmbed.pushState(this, state);
    }

}