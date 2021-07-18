package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.FuelConsumingPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.common.tileentities.HIVEControllerTileEntity;
import site.siredvin.progressiveperipherals.utils.LuaUtils;
import site.siredvin.progressiveperipherals.utils.RepresentationUtil;

import java.util.*;
import java.util.stream.Collectors;

public class HIVEControllerPeripheral extends FuelConsumingPeripheral {
    private final static String ENTITY_LIST_TAG = "entityList";
    private final static int radius = 8;
    private final HIVEControllerTileEntity tileEntity;

    public HIVEControllerPeripheral(String type, HIVEControllerTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return 1;
    }

    @Override
    protected int _getFuelConsumptionRate() {
        return 1;
    }

    @Override
    protected void _setFuelConsumptionRate(int rate) {

    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.emptyList();
    }

    protected Pair<MethodResult, MobEntity> getEntityByUUID(String entityUUID, boolean inControl) {
        World w = getWorld();
        if (!(w instanceof ServerWorld))
            return Pair.onlyLeft(MethodResult.of(null, "Well, this should happen, but seems to client-side execution"));
        ServerWorld world = (ServerWorld) w;
        Entity entity = world.getEntity(UUID.fromString(entityUUID));
        if (!(entity instanceof MobEntity))
            return Pair.onlyLeft(MethodResult.of(null, "cannot find entity"));
        if (!inControl && entity.blockPosition().distSqr(getPos()) > radius * radius)
            return Pair.onlyLeft(MethodResult.of(null, "cannot find entity"));
        MobEntity mobEntity = (MobEntity) entity;
        ItemStack currentHelmet = mobEntity.getItemBySlot(EquipmentSlotType.HEAD);
        if (currentHelmet.getItem() == Items.CONTROLLING_HELMET.get()) {
            if (!inControl)
                return Pair.onlyLeft(MethodResult.of(null, "Seems, this entity already on control"));
            CompoundNBT helmetData = currentHelmet.getOrCreateTag();
            if (!helmetData.getString(HIVEControllerTileEntity.CONTROLLED_ID_TAG).equals(tileEntity.getControllerID().toString()))
                return Pair.onlyLeft(MethodResult.of(null, "This entity controlled by another hive"));
        }
        return Pair.onlyRight(mobEntity);
    }

    protected ListNBT getEntityList() {
        return owner.getDataStorage().getList(ENTITY_LIST_TAG, 8); // list of strings
    }

    protected void saveEntityList(ListNBT entityList) {
        owner.getDataStorage().put(ENTITY_LIST_TAG, entityList);
    }

    @LuaFunction
    public final MethodResult researchSurrounding() {
        World world = getWorld();
        BlockPos pos = getPos();
        List<Entity> entities = world.getEntities((Entity) null, new AxisAlignedBB(pos).inflate(radius), entity -> entity instanceof LivingEntity && !(entity instanceof PlayerEntity));
        return MethodResult.of(entities.stream().map(entity -> RepresentationUtil.completeEntityWithPositionToLua(entity, ItemStack.EMPTY, pos)).collect(Collectors.toList()));
    }

    @LuaFunction
    public final MethodResult takeControl(String entityUUID) {
        Pair<MethodResult, MobEntity> searchResult = getEntityByUUID(entityUUID, false);
        if (searchResult.leftPresent())
            return searchResult.getLeft();
        MobEntity mobEntity = searchResult.getRight();
        mobEntity.setCustomName(new StringTextComponent(String.format("HIVE %s element", tileEntity.getControllerID())));
        ItemStack newHelmet = new ItemStack(Items.CONTROLLING_HELMET.get());
        CompoundNBT helmetData = newHelmet.getOrCreateTag();
        helmetData.putString(HIVEControllerTileEntity.CONTROLLED_ID_TAG, tileEntity.getControllerID().toString());
        mobEntity.setItemSlot(EquipmentSlotType.HEAD, newHelmet);
        ListNBT entityList = getEntityList();
        entityList.add(StringNBT.valueOf(entityUUID));
        saveEntityList(entityList);
        return MethodResult.of(true);
    }

    @LuaFunction
    public final List<String> elements() {
        ListNBT entityList = getEntityList();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++)
            result.add(entityList.getString(i));
        return result;
    }

    @LuaFunction
    public final MethodResult walk(String entityUUID, Map<?, ?> blockPos) throws LuaException {
        Pair<MethodResult, MobEntity> searchResult = getEntityByUUID(entityUUID, true);
        if (searchResult.leftPresent())
            return searchResult.getLeft();
        MobEntity entity = searchResult.getRight();
        BlockPos entityPosition = entity.blockPosition();
        BlockPos targetPos = LuaUtils.convertToBlockPos(entityPosition, blockPos);
        Brain<MobEntity> brain = (Brain<MobEntity>) entity.getBrain();
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, (float) entity.getMoveControl().getSpeedModifier(), 0));
        return MethodResult.of(true);
    }
}
