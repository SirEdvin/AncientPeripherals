package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.AbstractiumPedestalTileEntity;

import java.util.Optional;

public class AbstractiumPedestalPeripheral extends BasePeripheral {

    private final AbstractiumPedestalTileEntity tileEntity;

    public <T extends TileEntity & IPeripheralTileEntity> AbstractiumPedestalPeripheral(String type, AbstractiumPedestalTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableAbstractiumPedestal;
    }

    @LuaFunction
    public final MethodResult setItem(@NotNull IArguments arguments) throws LuaException {
        String item = arguments.getString(0);
        Optional<String> nameOptional = arguments.optString(1);
        Optional<Item> itemOptional = Registry.ITEM.getOptional(new ResourceLocation(item));
        if (!itemOptional.isPresent())
            throw new LuaException(String.format("Cannot find item %s", item));
        ItemStack stack = new ItemStack(itemOptional.get());
        nameOptional.ifPresent(name -> stack.setHoverName(new StringTextComponent(name)));
        tileEntity.setStoredStack(stack);
        return MethodResult.of(true);
    }

    @LuaFunction
    public final String getItem() {
        ItemStack stored = tileEntity.getStoredStack();
        if (stored.isEmpty())
            return "";
        ResourceLocation name = stored.getItem().getRegistryName();
        if (name == null)
            return "";
        return name.toString();
    }
}
