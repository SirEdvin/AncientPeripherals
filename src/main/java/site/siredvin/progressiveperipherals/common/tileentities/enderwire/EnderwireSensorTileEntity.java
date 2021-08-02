package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.Block;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireNetworkComponent;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireSensorBlock;

public class EnderwireSensorTileEntity extends BaseEnderwireTileEntity<EnderwireSensorTileEntity, BasePeripheral> {
    public EnderwireSensorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_SENSOR.get());
    }

    @Override
    public EnderwireSensorTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.SENSOR;
    }

    @Override
    public EnderwireNetworkComponent getComponentType() {
        if (level == null)
            return EnderwireNetworkComponent.UNKNOWN;
        Block block = level.getBlockState(worldPosition).getBlock();
        if (block instanceof IEnderwireSensorBlock)
            return ((IEnderwireSensorBlock) block).getComponentType();
        return EnderwireNetworkComponent.UNKNOWN;
    }
}
