package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryStructure;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery.FreeMachineryOperation.EXTRACT;

public class RBTExtractorPeripheral extends GenericMachineryPeripheral<RBTExtractorControllerTileEntity> {

    public RBTExtractorPeripheral(String type, RBTExtractorControllerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableExtractor;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(EXTRACT);
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("produceAmount", ProgressivePeripheralsConfig.extractorProduceAmount);
        data.put("consumeAmount", ProgressivePeripheralsConfig.extractorConsumeAmount);
        return data;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult extract() {
        World world = tileEntity.getLevel();
        Objects.requireNonNull(world);
        IMachineryStructure structure = tileEntity.getStructure();
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
        int currentCooldown = getCurrentCooldown(EXTRACT);
        if (currentCooldown > 0) {
            point.consumePower(ProgressivePeripheralsConfig.extractorConsumeAmount);
            return MethodResult.of(null, "You triggered production on cooldown, nothing will be produced by power will be consumed");
        }
        ItemStack productionResult = new ItemStack(producibleResource, ProgressivePeripheralsConfig.extractorProduceAmount);
        if (tileEntity.storeItem(productionResult, true).isEmpty())
            point.consumePower(ProgressivePeripheralsConfig.extractorConsumeAmount);
        trackOperation(EXTRACT, null);
        return MethodResult.of(true);
    }
}
