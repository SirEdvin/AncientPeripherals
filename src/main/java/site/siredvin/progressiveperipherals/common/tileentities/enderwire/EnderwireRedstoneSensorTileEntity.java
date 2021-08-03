package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireNetworkComponent;

import java.util.HashMap;
import java.util.Map;

public class EnderwireRedstoneSensorTileEntity extends BaseEnderwireTileEntity<EnderwireRedstoneSensorTileEntity, BasePeripheral> {

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
        return EnderwireElementType.SENSOR;
    }

    @Override
    public EnderwireNetworkComponent getComponentType() {
        return EnderwireNetworkComponent.REDSTONE_SENSOR;
    }

    public void setPower(Direction direction, int power) {
        powerBuffer[direction.ordinal()] = power;
    }

    public int getPower(Direction direction) {
        return powerBuffer[direction.ordinal()];
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        powerBuffer = tag.getIntArray(POWER_BUFFER_TAG);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = super.save(tag);
        tag.putIntArray(POWER_BUFFER_TAG, powerBuffer);
        return tag;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<String, Object>() {{
            for (Direction direction: Direction.values())
                put(direction.name().toLowerCase(), powerBuffer[direction.ordinal()]);
        }};
    }
}
