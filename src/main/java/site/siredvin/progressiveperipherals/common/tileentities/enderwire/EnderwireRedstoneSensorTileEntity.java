package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;

import java.util.HashMap;
import java.util.Map;

public class EnderwireRedstoneSensorTileEntity extends BaseEnderwireTileEntity<EnderwireRedstoneSensorTileEntity, BasePeripheral<?>> {

    private final static String POWER_BUFFER_TAG = "powerBuffer";

    private int[] powerBuffer;

    public EnderwireRedstoneSensorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_REDSTONE_SENSOR.get());
        powerBuffer = new int[6];
    }

    @Override
    public EnderwireRedstoneSensorTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.REDSTONE_SENSOR;
    }

    public void setPower(Direction direction, int power) {
        powerBuffer[direction.ordinal()] = power;
    }

    public int getPower(Direction direction) {
        return powerBuffer[direction.ordinal()];
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data) {
        super.loadInternalData(state, data);
        powerBuffer = data.getIntArray(POWER_BUFFER_TAG);
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data = super.saveInternalData(data);
        data.putIntArray(POWER_BUFFER_TAG, powerBuffer);
        return data;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<String, Object>() {{
            for (Direction direction: Direction.values())
                put(direction.name().toLowerCase(), powerBuffer[direction.ordinal()]);
        }};
    }
}
