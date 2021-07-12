package site.siredvin.ancientperipherals.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.AutomataCorePeripheral;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import site.siredvin.ancientperipherals.common.configuration.AncientPeripheralsConfig;
import site.siredvin.ancientperipherals.common.recipes.AutomataRecipe;
import site.siredvin.ancientperipherals.common.setup.Items;

import java.util.Map;
import java.util.Optional;

public class TrainableAutomataCorePeripheral extends AutomataCorePeripheral {

    private final static String COLLECT_XP_OPERATION = "collectXP";

    private final static String COLLECTED_XP_AMOUNT = "CollectedXPAmount";

    public TrainableAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return AncientPeripheralsConfig.trainableAutomataCoreMaxFuelConsumptionLevel;
    }

    @Override
    public int getInteractionRadius() {
        return AncientPeripheralsConfig.trainableAutomataCoreInteractionRadius;
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
          Map<String, Object> data = super.getPeripheralConfiguration();
          data.put("collectXPCost", AncientPeripheralsConfig.collectXPCost);
          data.put("collectXPCooldown", AncientPeripheralsConfig.collectXPCooldown);
          data.put("abstractiumXPPointsCost", AncientPeripheralsConfig.abstractiumXPPointsCost);
          return data;
    }

    @Override
    protected int getRawCooldown(String s) {
        if (s.equals(COLLECT_XP_OPERATION))
            return AncientPeripheralsConfig.collectXPCooldown;
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult collectXP() {
        Optional<MethodResult>  checkResults = cooldownCheck(COLLECT_XP_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        checkResults = consumeFuelOp(AncientPeripheralsConfig.collectXPCost);
        if (checkResults.isPresent()) return checkResults.get();
        addRotationCycle();
        World world = getWorld();
        BlockPos pos = getPos();
        AxisAlignedBB searchBox = new AxisAlignedBB(pos).inflate(getInteractionRadius());
        CompoundNBT data = owner.getDataStorage();
        int oldCount = data.getInt(COLLECTED_XP_AMOUNT);
        world.getEntitiesOfClass(ExperienceOrbEntity.class, searchBox).forEach(entity -> {
            data.putInt(COLLECTED_XP_AMOUNT, data.getInt(COLLECTED_XP_AMOUNT) + entity.value);
            entity.remove();
        });
        trackOperation(COLLECT_XP_OPERATION);
        return MethodResult.of(data.getInt(COLLECTED_XP_AMOUNT) - oldCount);
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
        CompoundNBT data = owner.getDataStorage();
        player.giveExperiencePoints(-suckedCount);
        data.putInt(COLLECTED_XP_AMOUNT, data.getInt(COLLECTED_XP_AMOUNT) + suckedCount);
        trackOperation(COLLECT_XP_OPERATION);
        return MethodResult.of(suckedCount);
    }

    @LuaFunction
    public final int getStoredXP() {
        return owner.getDataStorage().getInt(COLLECTED_XP_AMOUNT);
    }

    @LuaFunction
    public final MethodResult crystallizeXP(int count) {
        ItemStack result = new ItemStack(Items.ABSTRACTIUM_INGOT.get());
        if (count < result.getMaxStackSize())
            return MethodResult.of(null, "Can craft only one stack at time");
        int requiredXPAmount = count * AncientPeripheralsConfig.abstractiumXPPointsCost;
        CompoundNBT data = owner.getDataStorage();
        int currentAmount = data.getInt(COLLECTED_XP_AMOUNT);
        if (currentAmount < requiredXPAmount)
            return MethodResult.of(null, String.format("Not enough xp stored: %d/%d", currentAmount, requiredXPAmount));
        int freeSlot = -1;
        IInventory turtleInventory = turtle.getInventory();
        for (int slot = 0; slot < turtleInventory.getContainerSize(); slot++) {
            if (turtleInventory.getItem(slot).isEmpty()) {
                freeSlot = slot;
                break;
            }
        }
        if (freeSlot == -1)
            return MethodResult.of(null, "Cannot find free slot in turtle");
        data.putInt(COLLECTED_XP_AMOUNT, currentAmount - requiredXPAmount);
        result.setCount(count);
        turtleInventory.setItem(freeSlot, result);
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult craft() {
        IInventory inventory = turtle.getInventory();
        Optional<AutomataRecipe> optRecipe = getWorld().getRecipeManager().getRecipeFor(AutomataRecipe.TYPE, inventory, getWorld());
        if (!optRecipe.isPresent())
            return MethodResult.of(null, "Cannot find recipe");
        AutomataRecipe recipe = optRecipe.get();
        recipe.craft(inventory);
        return MethodResult.of(true);
    }
}
