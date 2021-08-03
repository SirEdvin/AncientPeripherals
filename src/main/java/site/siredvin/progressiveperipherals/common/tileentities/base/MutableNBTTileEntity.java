package site.siredvin.progressiveperipherals.common.tileentities.base;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;

import java.util.Objects;

public abstract class MutableNBTTileEntity<T extends BasePeripheral> extends OptionalPeripheralTileEntity<T> implements ITileEntityDataProvider {

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
        loadInternalData(getBlockState(), tag);
        if (isRequiredRenderUpdate())
            triggerRenderUpdate();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadInternalData(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = saveInternalData(tag);
        return super.save(tag);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public void pushInternalDataChangeToClient() {
        pushInternalDataChangeToClient(getBlockState());
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public void pushInternalDataChangeToClient(BlockState state) {
        World world = getLevel();
        Objects.requireNonNull(world);
        setChanged();
        world.setBlockAndUpdate(getBlockPos(), state);
        world.sendBlockUpdated(
                getBlockPos(), getBlockState(), getBlockState(),
                Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS
        );
    }

    public boolean isRequiredRenderUpdate() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void triggerRenderUpdate() {
        requestModelDataUpdate();
        BlockPos pos = getBlockPos();
        // Basically, just world.setBlocksDirty with bypass model block state check
        Minecraft.getInstance().levelRenderer.setBlocksDirty(
                pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ()
        );
    }

}
