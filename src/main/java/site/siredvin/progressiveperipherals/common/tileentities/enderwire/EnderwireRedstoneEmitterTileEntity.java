package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;

import java.util.HashMap;
import java.util.Map;

public class EnderwireRedstoneEmitterTileEntity extends BaseEnderwireTileEntity<EnderwireRedstoneEmitterTileEntity, BasePeripheral> {

    private final static String POWER_BUFFER_TAG = "powerBuffer";

    private int[] powerBuffer;

    public EnderwireRedstoneEmitterTileEntity() {
        super(TileEntityTypes.ENDERWIRE_REDSTONE_EMITTER.get());
        powerBuffer = new int[6];
    }

    @Override
    public EnderwireRedstoneEmitterTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.REDSTONE_EMITTER;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    public void setPower(Direction direction, int power) {
        powerBuffer[direction.ordinal()] = power;
        pushInternalDataChangeToClient();
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
}
