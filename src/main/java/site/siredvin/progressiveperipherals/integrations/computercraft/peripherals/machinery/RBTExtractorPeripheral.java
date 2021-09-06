package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.lib.peripherals.owner.OperationAbility;
import de.srendi.advancedperipherals.lib.peripherals.owner.PeripheralOwnerAbility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryStructure;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;
import site.siredvin.progressiveperipherals.utils.ValueContainer;

import java.util.Map;
import java.util.Objects;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.EXTRACT;

public class RBTExtractorPeripheral extends GenericMachineryPeripheral<RBTExtractorControllerTileEntity> {

    public static final String TYPE = "realityBreakthroughExtractorController";

    public RBTExtractorPeripheral(RBTExtractorControllerTileEntity tileEntity) {
        super(TYPE, tileEntity);
        OperationAbility operationAbility = owner.getAbility(PeripheralOwnerAbility.OPERATION);
        Objects.requireNonNull(operationAbility);
        operationAbility.registerOperation(EXTRACT);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableExtractor;
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("produceAmount", ProgressivePeripheralsConfig.extractorProduceAmount);
        data.put("consumeAmount", ProgressivePeripheralsConfig.extractorConsumeAmount);
        return data;
    }

    // TODO: refactor after new AP release

    @LuaFunction(mainThread = true)
    public final MethodResult extract() throws LuaException {
        OperationAbility operationAbility = owner.getAbility(PeripheralOwnerAbility.OPERATION);
        World world = owner.tileEntity.getLevel();
        Objects.requireNonNull(operationAbility);
        Objects.requireNonNull(world);

        ValueContainer<RealityBreakthroughPointTileEntity> pointHolder = new ValueContainer<>(null);
        ValueContainer<Item> itemHolder = new ValueContainer<>(null);

        MethodResult result = withOperation(EXTRACT, null, ignored -> {
            IMachineryStructure structure = owner.tileEntity.getStructure();
            if (structure == null)
                return MethodResult.of(null, "Machine should be configured first");
            TileEntity pointEntity = world.getBlockEntity(structure.getCenter());
            if (!(pointEntity instanceof RealityBreakthroughPointTileEntity))
                return MethodResult.of(null, "Cannot find breakthrough point");
            RealityBreakthroughPointTileEntity point = (RealityBreakthroughPointTileEntity) pointEntity;
            if (!point.isDecrypted())
                return MethodResult.of(null, "Point should be decrypted");
            if (point.getPowerLevel() < ProgressivePeripheralsConfig.extractorConsumeAmount)
                return MethodResult.of(null, "Not enough power in point");
            if (!point.canProduceResource())
                return MethodResult.of(null, "Point cannot produce resource");
            Item producibleResource = point.getProducibleResource();
            if (producibleResource == null)
                return MethodResult.of(null, "Point cannot produce resource");
            pointHolder.setValue(point);
            itemHolder.setValue(producibleResource);
            return null;
        }, ignored -> {
            RealityBreakthroughPointTileEntity point = pointHolder.getNotNull();
            Item producibleResource = itemHolder.getNotNull();
            ItemStack productionResult = new ItemStack(producibleResource, ProgressivePeripheralsConfig.extractorProduceAmount);
            if (owner.tileEntity.storeItem(productionResult, true).isEmpty())
                point.consumePower(ProgressivePeripheralsConfig.extractorConsumeAmount);
            return MethodResult.of(true);
        }, null);

        Object[] results = result.getResult();
        if (results != null && results.length == 2 && results[0] == null && results[1].toString().endsWith("is on cooldown") && pointHolder.isPresent()) {
            pointHolder.getNotNull().consumePower(ProgressivePeripheralsConfig.extractorConsumeAmount);
            return MethodResult.of(null, "You triggered production on cooldown, nothing will be produced by power will be consumed");
        }

        return result;
    }
}
