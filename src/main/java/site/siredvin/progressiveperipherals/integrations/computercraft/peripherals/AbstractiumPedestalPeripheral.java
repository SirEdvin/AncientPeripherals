package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
        return true;
    }

    @LuaFunction
    public final MethodResult setItem(String name) throws LuaException {
        Optional<Item> itemOptional = Registry.ITEM.getOptional(new ResourceLocation(name));
        if (!itemOptional.isPresent())
            throw new LuaException(String.format("Cannot find item %s", name));
        tileEntity.setStoredStack(new ItemStack(itemOptional.get()));
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
