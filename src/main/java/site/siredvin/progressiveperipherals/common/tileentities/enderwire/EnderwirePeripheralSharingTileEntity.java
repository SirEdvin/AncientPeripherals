package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireDirectionalBlock;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwirePeripheralSharingTileEntity extends BaseEnderwireWiredTileEntity<EnderwirePeripheralSharingTileEntity> {

    public EnderwirePeripheralSharingTileEntity() {
        super(TileEntityTypes.ENDERWIRE_PERIPHERAL_SHARING.get());
    }

    @Override
    public void onNeighbourChange(@Nonnull BlockPos neighbour) {
        onNeighbourTileEntityChange(neighbour);
    }

    public Direction getLookingDirection() {
        BlockState state = getBlockState();
        return state.getValue(EnderwireDirectionalBlock.FACING);
    }

    @Override
    public void onNeighbourTileEntityChange(@Nonnull BlockPos neighbour) {
        Objects.requireNonNull(level);
        if (!level.isClientSide) {
            Direction facing = getLookingDirection();
            if (getBlockPos().relative(facing).equals(neighbour))
                refreshPeripheral();
        }
    }

    @Override
    protected void refreshPeripheral() {
        if (level != null && !isRemoved() && localPeripheral.attach(level, getBlockPos(), getLookingDirection())) {
            updateConnectedPeripherals();
        }
    }

    @Override
    public void blockTick() {
        super.blockTick();
        if (!initialized) {
            refreshPeripheral();
            initialized = true;
        }
    }

    private void updateConnectedPeripherals() {
        Map<String, IPeripheral> peripherals = localPeripheral.toMap();
        node.updatePeripherals(peripherals);
    }

    @Override
    public EnderwirePeripheralSharingTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.PERIPHERAL_SHARING;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<String, Object>() {{
            IPeripheral sharedPeripheral = localPeripheral.getPeripheral();
            if (sharedPeripheral != null) {
                put("sharedPeripheral", sharedPeripheral.getType());
            } else {
                put("sharedPeripheral", null);
            }
        }};
    }

    @Override
    public @NotNull Vector3d getWiredPosition() {
        BlockPos pos = getBlockPos().relative(getLookingDirection());
        return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }
}
