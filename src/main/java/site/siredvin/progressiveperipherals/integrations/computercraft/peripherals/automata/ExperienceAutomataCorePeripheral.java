package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.AutomataCorePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.utils.CheckUtils;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.XP_TRANSFER;

public abstract class ExperienceAutomataCorePeripheral extends AutomataCorePeripheral {
    protected final static String COLLECTED_XP_AMOUNT = "CollectedXPAmount";

    public ExperienceAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("xpToFuelRate", ProgressivePeripheralsConfig.xpToFuelRate);
        return data;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return new ArrayList<IPeripheralOperation<?>>() {{
            add(XP_TRANSFER);
        }};
    }

    protected double _getStoredXP() {
        return _getStoredXP(owner.getDataStorage());
    }

    protected double _getStoredXP(CompoundNBT data) {
        return data.getDouble(COLLECTED_XP_AMOUNT);
    }

    protected void adjustStoredXP(double amount) {
        adjustStoredXP(amount, owner.getDataStorage());
    }

    protected void adjustStoredXP(double amount, CompoundNBT data) {
        data.putDouble(COLLECTED_XP_AMOUNT, data.getDouble(COLLECTED_XP_AMOUNT) + amount);
    }

    protected MethodResult withOperation(SimpleOperation operation, Function<Object, MethodResult> function) {
        return this.withOperation(operation, null, function, null);
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult collectXP() {
        return withOperation(XP_TRANSFER, context -> {
            World world = getWorld();
            BlockPos pos = getPos();
            AxisAlignedBB searchBox = new AxisAlignedBB(pos).inflate(getInteractionRadius());
            CompoundNBT data = owner.getDataStorage();
            double oldCount = _getStoredXP(data);
            world.getEntitiesOfClass(ExperienceOrbEntity.class, searchBox).forEach(entity -> {
                adjustStoredXP(entity.value, data);
                entity.remove();
            });
            return MethodResult.of(_getStoredXP(data) - oldCount);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult suckOwnerXP(int limit) {
        return withOperation(XP_TRANSFER, context -> {
            PlayerEntity player = owner.getOwner();
            if (player == null)
                return MethodResult.of(null, "Cannot find owning player");
            int suckedCount = Math.min(player.totalExperience, limit);
            player.giveExperiencePoints(-suckedCount);
            adjustStoredXP(suckedCount);
            return MethodResult.of(suckedCount);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final double burnXP(double limit) throws LuaException {
        if (limit <= 0)
            throw new LuaException("Incorrect limit");
        double burnAmount = Math.min(limit, _getStoredXP());
        adjustStoredXP(-burnAmount);
        addFuel((int) (burnAmount * ProgressivePeripheralsConfig.xpToFuelRate));
        return burnAmount;
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult sendXPToOwner(int limit) {
        return withOperation(XP_TRANSFER, context -> {
            double count = Math.min(limit, _getStoredXP());
            addRotationCycle();
            PlayerEntity player = owner.getOwner();
            if (player == null)
                return MethodResult.of(null, "Cannot find owning player");
            player.giveExperiencePoints((int) count);
            adjustStoredXP(-count);
            return MethodResult.of(count);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult sendXP(Map<?, ?> rawBlockPos, double limit) throws LuaException {
        BlockPos pos = getPos();
        BlockPos targetPos = LuaUtils.convertToBlockPos(pos, rawBlockPos);
        return withOperation(XP_TRANSFER, context -> {
            if (!CheckUtils.radiusCorrect(pos, targetPos, getInteractionRadius()))
                return MethodResult.of(null, "Turtle are too far away");
            TileEntity entity = getWorld().getBlockEntity(targetPos);
            if (!(entity instanceof TileTurtle))
                return MethodResult.of(null, "Target block is not turtle");
            ITurtleAccess targetTurtle = ((TileTurtle) entity).getAccess();
            IPeripheral targetPeripheral = targetTurtle.getPeripheral(TurtleSide.LEFT);
            if (!(targetPeripheral instanceof ExperienceAutomataCorePeripheral)) {
                targetPeripheral = targetTurtle.getPeripheral(TurtleSide.RIGHT);
            }
            if (!(targetPeripheral instanceof ExperienceAutomataCorePeripheral)) {
                return MethodResult.of(null, "Turtle should have upgrade that support XP transfering methods");
            }
            double transferAmount = Math.min(_getStoredXP(), limit);
            adjustStoredXP(-transferAmount);
            ((ExperienceAutomataCorePeripheral) targetPeripheral).adjustStoredXP(transferAmount);
            return MethodResult.of(transferAmount);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final double getStoredXP() {
        return _getStoredXP();
    }

    @SuppressWarnings("unused")
    @LuaFunction
    public final MethodResult getOwnerXP() {
        PlayerEntity player = owner.getOwner();
        if (player == null)
            return MethodResult.of(null, "Cannot find owning player");
        return MethodResult.of(player.totalExperience);
    }
}
