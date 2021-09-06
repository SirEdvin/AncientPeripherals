package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import de.srendi.advancedperipherals.lib.peripherals.*;
import de.srendi.advancedperipherals.lib.peripherals.owner.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.PPAbilities;
import site.siredvin.progressiveperipherals.utils.CheckUtils;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.Map;
import java.util.Objects;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.XP_TRANSFER;

public class ExperienceAbility implements IOwnerAbility, IPeripheralPlugin {

    protected final static String COLLECTED_XP_AMOUNT = "CollectedXPAmount";

    private final AutomataCorePeripheral automataCore;

    public ExperienceAbility(AutomataCorePeripheral automataCore) {
        this.automataCore = automataCore;
    }

    @Override
    public @Nullable IPeripheralOperation<?>[] getOperations() {
        return new IPeripheralOperation[]{XP_TRANSFER};
    }

    @Override
    public void collectConfiguration(Map<String, Object> data) {
        data.put("xpToFuelRate", ProgressivePeripheralsConfig.xpToFuelRate);
    }

    protected double _getStoredXP() {
        return _getStoredXP(automataCore.getPeripheralOwner().getDataStorage());
    }

    protected double _getStoredXP(CompoundNBT data) {
        return data.getDouble(COLLECTED_XP_AMOUNT);
    }

    protected void adjustStoredXP(double amount) {
        adjustStoredXP(amount, automataCore.getPeripheralOwner().getDataStorage());
    }

    protected void adjustStoredXP(double amount, CompoundNBT data) {
        data.putDouble(COLLECTED_XP_AMOUNT, data.getDouble(COLLECTED_XP_AMOUNT) + amount);
    }

    protected MethodResult withOperation(SimpleOperation operation, IPeripheralFunction<Object, MethodResult> function) throws LuaException {
        OperationAbility ability = automataCore.getPeripheralOwner().getAbility(PeripheralOwnerAbility.OPERATION);
        Objects.requireNonNull(ability);
        return ability.performOperation(operation, null, null, function, null);
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult collectXP() throws LuaException {
        return withOperation(XP_TRANSFER, context -> {
            TurtlePeripheralOwner owner = automataCore.getPeripheralOwner();
            World world = owner.getWorld();
            BlockPos pos = owner.getPos();
            AxisAlignedBB searchBox = new AxisAlignedBB(pos).inflate(automataCore.getInteractionRadius());
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
    public final MethodResult suckOwnerXP(int limit) throws LuaException {
        return withOperation(XP_TRANSFER, context -> {
            PlayerEntity player = automataCore.getPeripheralOwner().getOwner();
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
        FuelAbility<?> fuelAbility = automataCore.getPeripheralOwner().getAbility(PeripheralOwnerAbility.FUEL);
        if (fuelAbility == null)
            throw new LuaException("Unsupported operation");
        double burnAmount = Math.min(limit, _getStoredXP());
        adjustStoredXP(-burnAmount);
        fuelAbility.addFuel((int) (burnAmount * ProgressivePeripheralsConfig.xpToFuelRate));
        return burnAmount;
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult sendXPToOwner(int limit) throws LuaException {
        return withOperation(XP_TRANSFER, context -> {
            double count = Math.min(limit, _getStoredXP());
            automataCore.addRotationCycle();
            PlayerEntity player = automataCore.getPeripheralOwner().getOwner();
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
        TurtlePeripheralOwner owner = automataCore.getPeripheralOwner();;
        BlockPos pos = owner.getPos();
        BlockPos targetPos = LuaUtils.convertToBlockPos(pos, rawBlockPos);
        return withOperation(XP_TRANSFER, context -> {
            if (!CheckUtils.radiusCorrect(pos, targetPos, automataCore.getInteractionRadius()))
                return MethodResult.of(null, "Turtle are too far away");
            TileEntity entity = owner.getWorld().getBlockEntity(targetPos);
            if (!(entity instanceof TileTurtle))
                return MethodResult.of(null, "Target block is not turtle");
            ITurtleAccess targetTurtle = ((TileTurtle) entity).getAccess();
            IPeripheral targetPeripheral = targetTurtle.getPeripheral(TurtleSide.LEFT);
            if (!(targetPeripheral instanceof IBasePeripheral)) {
                targetPeripheral = targetTurtle.getPeripheral(TurtleSide.RIGHT);
            }
            if (!(targetPeripheral instanceof IBasePeripheral)) {
                return MethodResult.of(null, "Turtle should have upgrade that support XP transfering methods");
            }
            IPeripheralOwner targetOwner = ((IBasePeripheral<?>) targetPeripheral).getPeripheralOwner();
            ExperienceAbility targetAbility = targetOwner.getAbility(PPAbilities.EXPERIENCE);
            if (targetAbility == null)
                return MethodResult.of(null, "Turtle should have upgrade that support XP transfering methods");
            double transferAmount = Math.min(_getStoredXP(), limit);
            adjustStoredXP(-transferAmount);
            targetAbility.adjustStoredXP(transferAmount);
            return MethodResult.of(transferAmount);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final double getStoredXP() {
        return _getStoredXP();
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult getOwnerXP() {
        PlayerEntity player = automataCore.getPeripheralOwner().getOwner();
        if (player == null)
            return MethodResult.of(null, "Cannot find owning player");
        return MethodResult.of(player.totalExperience);
    }
}
