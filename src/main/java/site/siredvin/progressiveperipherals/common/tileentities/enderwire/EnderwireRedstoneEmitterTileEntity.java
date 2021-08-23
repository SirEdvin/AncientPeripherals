package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.core.computer.ComputerSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireRedstoneEmitterBlock;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.utils.OrientationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnderwireRedstoneEmitterTileEntity extends BaseEnderwireTileEntity<EnderwireRedstoneEmitterTileEntity, BasePeripheral> {

    private final static String POWER_BUFFER_TAG = "powerBuffer";
    private final static ComputerSide[] ALLOWED_SIDES = new ComputerSide[]{
            ComputerSide.LEFT, ComputerSide.RIGHT, ComputerSide.FRONT, ComputerSide.BACK
    };

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
        BlockState state = getBlockState();
        Direction facing = state.getValue(EnderwireRedstoneEmitterBlock.FACING);
        AttachFace face = state.getValue(EnderwireRedstoneEmitterBlock.FACE);
        Map<String, Object> data = new HashMap<>();
        for (ComputerSide side: ALLOWED_SIDES) {
            data.put(side.name().toLowerCase(), powerBuffer[OrientationUtils.toOffset(facing, face, side).ordinal()]);
        }
        return data;
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
    public MethodResult configure(Map<?, ?> data) throws LuaException {
        BlockState state = getBlockState();
        Direction facing = state.getValue(EnderwireRedstoneEmitterBlock.FACING);
        AttachFace face = state.getValue(EnderwireRedstoneEmitterBlock.FACE);
        List<Direction> updatedSides = new ArrayList<>();
        for (ComputerSide side: ALLOWED_SIDES) {
            Object power = data.get(side.name().toLowerCase());
            if (power != null) {
                if (!(power instanceof Number))
                    throw new LuaException("Power should be instance of number");
                int realPower = ((Number) power).intValue();
                if (realPower < 0 || realPower > 15)
                    throw new LuaException("Power should be from 0 to 15");
                Direction offset = OrientationUtils.toOffset(facing, face, side);
                if (powerBuffer[offset.ordinal()] != realPower) {
                    powerBuffer[offset.ordinal()] = realPower;
                    updatedSides.add(offset);
                }
            }
        }
        pushRedstoneUpdate(updatedSides);
        return MethodResult.of(true);
    }
}
