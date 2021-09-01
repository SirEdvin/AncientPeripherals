package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.util.LuaConverter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.*;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUERY_REGISTRY;

public class ItemRegistryPeripheral extends OperationPeripheral {
    public static final String TYPE = "itemRegistry";

    public <T extends TileEntity & IPeripheralTileEntity> ItemRegistryPeripheral(T tileEntity) {
        super(TYPE, tileEntity);
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.singletonList(QUERY_REGISTRY);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableItemRegistry;
    }

    @LuaFunction
    public final MethodResult getItems() {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();
        trackOperation(QUERY_REGISTRY, null);
        return MethodResult.of(LuaUtils.toLua(Registry.ITEM.keySet().stream().map(ResourceLocation::toString)));
    }

    @LuaFunction
    public final MethodResult getItemDescription(@NotNull IArguments arguments) throws LuaException {
        Optional<MethodResult> checkResult = this.cooldownCheck(QUERY_REGISTRY);
        if (checkResult.isPresent())
            return checkResult.get();
        ResourceLocation itemID = LuaUtils.getResourceLocation(arguments, 0);
        Optional<Item> optItem = Registry.ITEM.getOptional(itemID);
        if (!optItem.isPresent())
            return MethodResult.of(null, String.format("Cannot find item with id %s", itemID));
        Item item = optItem.get();
        Map<String, Object> data = new HashMap<>();
        data.put("tags", LuaConverter.tagsToList(item.getTags()));
        data.put("id", itemID.toString());
        data.put("displayName", item.getName(new ItemStack(item)).getString());
        trackOperation(QUERY_REGISTRY, null);
        return MethodResult.of(data);
    }
}
