package site.siredvin.ancientperipherals.computercraft.peripherals.abstractions;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.AutomataCorePeripheral;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;

import java.util.Map;
import java.util.Optional;

public abstract class ExperienceAutomataCorePeripheral extends AutomataCorePeripheral {
    protected final static String COLLECT_XP_OPERATION = "collectXP";
    protected final static String COLLECTED_XP_AMOUNT = "CollectedXPAmount";

    public ExperienceAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    protected int getRawCooldown(String s) {
        if (s.equals(COLLECT_XP_OPERATION))
            return AncientPeripheralsConfig.collectXPCooldown;
        return 0;
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("collectXPCost", AncientPeripheralsConfig.collectXPCost);
        data.put("collectXPCooldown", AncientPeripheralsConfig.collectXPCooldown);
        return data;
    }

    protected int _getStoredXP() {
        return _getStoredXP(owner.getDataStorage());
    }

    protected int _getStoredXP(CompoundNBT data) {
        return data.getInt(COLLECTED_XP_AMOUNT);
    }

    protected void adjustStoredXP(int amount) {
        adjustStoredXP(amount, owner.getDataStorage());
    }

    protected void adjustStoredXP(int amount, CompoundNBT data) {
        data.putInt(COLLECTED_XP_AMOUNT, data.getInt(COLLECTED_XP_AMOUNT) + amount);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult collectXP() {
        Optional<MethodResult> checkResults = cooldownCheck(COLLECT_XP_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        checkResults = consumeFuelOp(AncientPeripheralsConfig.collectXPCost);
        if (checkResults.isPresent()) return checkResults.get();
        addRotationCycle();
        World world = getWorld();
        BlockPos pos = getPos();
        AxisAlignedBB searchBox = new AxisAlignedBB(pos).inflate(getInteractionRadius());
        CompoundNBT data = owner.getDataStorage();
        int oldCount = _getStoredXP(data);
        world.getEntitiesOfClass(ExperienceOrbEntity.class, searchBox).forEach(entity -> {
            adjustStoredXP(entity.value, data);
            entity.remove();
        });
        trackOperation(COLLECT_XP_OPERATION);
        return MethodResult.of(_getStoredXP(data) - oldCount);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult suckOwnerXP(int count) {
        Optional<MethodResult>  checkResults = cooldownCheck(COLLECT_XP_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        checkResults = consumeFuelOp(AncientPeripheralsConfig.collectXPCost);
        if (checkResults.isPresent()) return checkResults.get();
        addRotationCycle();
        PlayerEntity player = owner.getOwner();
        if (player == null)
            return MethodResult.of(null, "Cannot find owning player");
        int suckedCount = Math.min(player.totalExperience, count);
        player.giveExperiencePoints(-suckedCount);
        adjustStoredXP(suckedCount);
        trackOperation(COLLECT_XP_OPERATION);
        return MethodResult.of(suckedCount);
    }

    @LuaFunction
    public final int getStoredXP() {
        return _getStoredXP();
    }
}
