package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.Block;
import net.minecraft.state.properties.BlockStateProperties;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireSensorBlock;

import java.util.HashMap;
import java.util.Map;

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
        Block block = getBlockState().getBlock();
        if (block instanceof IEnderwireSensorBlock)
            return ((IEnderwireSensorBlock) block).getComponentType();
        throw new IllegalArgumentException("How this even happened?");
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<String, Object>() {{
            put("powered", getBlockState().getValue(BlockStateProperties.POWERED));
        }};
    }
}
