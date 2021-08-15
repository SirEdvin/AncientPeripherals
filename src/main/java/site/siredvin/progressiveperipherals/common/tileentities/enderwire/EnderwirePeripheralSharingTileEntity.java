package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.Peripherals;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IBasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireDirectionalBlock;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkEvent;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwirePeripheralSharingTileEntity extends BaseEnderwireTileEntity<EnderwirePeripheralSharingTileEntity, IBasePeripheral> {

    private boolean initialized = false;
    private @Nullable IPeripheral sharedPeripheral;

    public EnderwirePeripheralSharingTileEntity() {
        super(TileEntityTypes.ENDERWIRE_PERIPHERAL_SHARING.get());
    }

    private void detachCurrentPeripheral() {
        ProgressivePeripherals.LOGGER.warn(String.format("Try to cleanup shared peripheral data for %s", getElementName()));
        if (level != null && !level.isClientSide && sharedPeripheral != null && attachedNetwork != null) {
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            if (network == null)
                return;
            EnderwireNetworkElement element = network.getElement(getElementName());
            if (element == null)
                return;
            ProgressivePeripherals.LOGGER.warn(String.format("Firing cleanup for %s", getElementName()));
            EnderwireNetworkBusHub.fireNetworkEvent(attachedNetwork, new EnderwireNetworkEvent.PeripheralDetached(element, sharedPeripheral));
            sharedPeripheral = null;
        }
    }

    private void attachNewPeripheral(@NotNull IPeripheral newPeripheral) {
        ProgressivePeripherals.LOGGER.warn(String.format("Try to attach new peripherals shared peripheral data for %s", getElementName()));
        if (level != null && !level.isClientSide && attachedNetwork != null) {
            GlobalNetworksData networks = GlobalNetworksData.get((ServerWorld) level);
            EnderwireNetwork network = networks.getNetwork(attachedNetwork);
            if (network == null)
                return;
            EnderwireNetworkElement element = network.getElement(getElementName());
            if (element == null)
                return;
            sharedPeripheral = newPeripheral;
            ProgressivePeripherals.LOGGER.warn(String.format("Peripheral attached to %s", getElementName()));
            EnderwireNetworkBusHub.fireNetworkEvent(attachedNetwork, new EnderwireNetworkEvent.PeripheralAttached(element, sharedPeripheral));
        }
    }

    public void destroy() {
        detachCurrentPeripheral();
    }

    @Override
    public void placed() {
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void onChunkUnloaded() {
        detachCurrentPeripheral();
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved() {
        detachCurrentPeripheral();
        super.setRemoved();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        SingleTickScheduler.schedule(this);
    }

    @Override
    public void clearCache() {
        super.clearCache();
        SingleTickScheduler.now(this);
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

    protected void refreshPeripheral() {
        if (level != null && !isRemoved()) {
            Direction lookingDirection = getLookingDirection();
            IPeripheral peripheral = Peripherals.getPeripheral(level, getBlockPos().relative(lookingDirection), lookingDirection.getOpposite(), optPeripheral -> refreshPeripheral());

            if (peripheral == null) {
                if (sharedPeripheral != null)
                    detachCurrentPeripheral();
            } else if (!peripheral.equals(sharedPeripheral)) {
                if (sharedPeripheral != null)
                    detachCurrentPeripheral();
                attachNewPeripheral(peripheral);
            }
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

    @Override
    public EnderwirePeripheralSharingTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.PERIPHERAL_SHARING;
    }

    @Nullable
    @Override
    public IPeripheral getSharedPeripheral() {
        return sharedPeripheral;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<String, Object>() {{
            if (sharedPeripheral != null) {
                put("sharedPeripheral", sharedPeripheral.getType());
            } else {
                put("sharedPeripheral", null);
            }
        }};
    }
}
