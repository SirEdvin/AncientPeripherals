package site.siredvin.progressiveperipherals.common.tileentities.base;

import dan200.computercraft.shared.util.RedstoneUtil;
import de.srendi.advancedperipherals.lib.peripherals.IBasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;

import java.util.List;
import java.util.Objects;

public abstract class MutableNBTTileEntity<T extends IBasePeripheral<?>> extends OptionalPeripheralTileEntity<T> implements ITileEntityDataProvider {

    public MutableNBTTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public @NotNull CompoundNBT getUpdateTag() {
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
    public void load(@NotNull BlockState state, @NotNull CompoundNBT tag) {
        super.load(state, tag);
        loadInternalData(state, tag);
    }

    @Override
    public @NotNull CompoundNBT save(@NotNull CompoundNBT tag) {
        tag = saveInternalData(tag);
        return super.save(tag);
    }

    @Override
    public void pushInternalDataChangeToClient() {
        pushInternalDataChangeToClient(getBlockState());
    }

    @Override
    public void pushInternalDataChangeToClient(BlockState state) {
        World world = getLevel();
        Objects.requireNonNull(world);
        if (!world.isClientSide) {
            setChanged();
            world.setBlockAndUpdate(getBlockPos(), state);
            world.sendBlockUpdated(
                    getBlockPos(), getBlockState(), getBlockState(),
                    Constants.BlockFlags.DEFAULT
            );
        }
    }

    public void pushRedstoneUpdate(List<Direction> neighbors) {
        World world = getLevel();
        Objects.requireNonNull(world);
        if (!world.isClientSide) {
            BlockPos pos = getBlockPos();
            for (Direction neighbor : neighbors) {
                RedstoneUtil.propagateRedstoneOutput(world, pos, neighbor);
            }
        }
    }

    public boolean isRequiredRenderUpdate() {
        return false;
    }

    @Override
    public void triggerRenderUpdate() {
        World world = getLevel();
        Objects.requireNonNull(world);
        if (world.isClientSide) {
            requestModelDataUpdate();
            BlockPos pos = getBlockPos();
            // Basically, just world.setBlocksDirty with bypass model block state check
            Minecraft.getInstance().levelRenderer.setBlocksDirty(
                    pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ()
            );
        }
    }

}
