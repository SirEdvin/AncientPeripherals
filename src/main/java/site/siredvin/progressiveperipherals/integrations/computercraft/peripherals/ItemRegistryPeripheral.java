package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.util.LuaConverter;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TileEntityPeripheralOwner;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.ItemRegistryTileEntity;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.QUERY_REGISTRY;

public class ItemRegistryPeripheral extends BasePeripheral<TileEntityPeripheralOwner<ItemRegistryTileEntity>> {
    public static final String TYPE = "itemRegistry";

    public ItemRegistryPeripheral(ItemRegistryTileEntity tileEntity) {
        super(TYPE, new TileEntityPeripheralOwner<>(tileEntity));
        owner.attachOperation(QUERY_REGISTRY);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableItemRegistry;
    }

    @LuaFunction
    public final MethodResult getItems() throws LuaException {
        return withOperation(
                QUERY_REGISTRY, null, null,
                ignored -> MethodResult.of(LuaUtils.toLua(Registry.ITEM.keySet().stream().map(ResourceLocation::toString))), null
        );
    }

    @LuaFunction
    public final MethodResult getItemDescription(@NotNull IArguments arguments) throws LuaException {
        return withOperation(QUERY_REGISTRY, null, null, ignored -> {
            ResourceLocation itemID = LuaUtils.getResourceLocation(arguments, 0);
            Optional<Item> optItem = Registry.ITEM.getOptional(itemID);
            if (!optItem.isPresent())
                return MethodResult.of(null, String.format("Cannot find item with id %s", itemID));
            Item item = optItem.get();
            Map<String, Object> data = new HashMap<>();
            data.put("tags", LuaConverter.tagsToList(item.getTags()));
            data.put("id", itemID.toString());
            data.put("displayName", item.getName(new ItemStack(item)).getString());
            return MethodResult.of(data);
        }, null);
    }
}
