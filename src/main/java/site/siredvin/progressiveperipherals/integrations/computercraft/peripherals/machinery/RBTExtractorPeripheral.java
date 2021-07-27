package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryStructure;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RBTExtractorControllerTileEntity;
import site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery.RealityBreakthroughPointTileEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RBTExtractorPeripheral extends GenericMachineryPeripheral<RBTExtractorControllerTileEntity> {

    public RBTExtractorPeripheral(String type, RBTExtractorControllerTileEntity tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(FreeMachineryOperation.EXTRACT);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult extract() {
        // TODO: add cooldown and power loss when cooldown is active
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
        if (!point.canProduceResource())
            return MethodResult.of(null, "Point cannot produce resource");
        Item producibleResource = point.getProducibleResource();
        if (producibleResource == null)
            return MethodResult.of(null, "Point cannot produce resource");
        // TODO: add configuration for convertion rate
        ItemStack productionResult = new ItemStack(producibleResource, 1);
        if (tileEntity.storeItem(productionResult, true).isEmpty())
            point.consumePower(1);
        return MethodResult.of(true);
    }
}
