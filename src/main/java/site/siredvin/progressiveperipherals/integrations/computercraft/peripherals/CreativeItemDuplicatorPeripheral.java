package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.srendi.advancedperipherals.common.util.InventoryUtil;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TileEntityPeripheralOwner;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.CreativeItemDuplicatorTileEntity;

import javax.annotation.Nullable;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.FreeOperation.DUPLICATE_ITEM;

public class CreativeItemDuplicatorPeripheral extends BasePeripheral<TileEntityPeripheralOwner<CreativeItemDuplicatorTileEntity>> {
    public static final String TYPE = "creativeItemDuplicator";

    private final CreativeItemDuplicatorTileEntity tileEntity;

    private static IItemHandler extractHandler(@Nullable Object object) {
        if (object instanceof ICapabilityProvider) {
            LazyOptional<IItemHandler> cap = ((ICapabilityProvider) object).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if (cap.isPresent()) return cap.orElseThrow(NullPointerException::new);
        }
        if (object instanceof IItemHandler)
            return (IItemHandler) object;
        if (object instanceof IInventory)
            return new InvWrapper((IInventory) object);
        return null;
    }

    private class UnlimitedItemHandler implements IItemHandler {

        @Override
        public int getSlots() {
            return 1;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return tileEntity.getStoredStack();
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack stackCopy = tileEntity.getStoredStack().copy();
            stackCopy.setCount(amount);
            return stackCopy;
        }

        @Override
        public int getSlotLimit(int slot) {
            return tileEntity.getStoredStack().getItem().getItemStackLimit(tileEntity.getStoredStack());
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }


    public CreativeItemDuplicatorPeripheral(CreativeItemDuplicatorTileEntity tileEntity) {
        super(TYPE, new TileEntityPeripheralOwner<>(tileEntity));
        owner.attachOperation(DUPLICATE_ITEM);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableCreativeItemDuplicator;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult pushItem(@NotNull IComputerAccess access, @NotNull IArguments arguments) throws LuaException {
        String toName = arguments.getString(0);
        int limit = arguments.optInt(1, Integer.MAX_VALUE);
        int toSlot = arguments.optInt(2, -1);
        return withOperation(DUPLICATE_ITEM, null, null, ingored -> {
            // Find location to transfer to
            IPeripheral location = access.getAvailablePeripheral(toName);
            if (location == null) throw new LuaException("Target '" + toName + "' does not exist");

            IItemHandler to = extractHandler(location.getTarget());
            if (to == null) throw new LuaException("Target '" + toName + "' is not an inventory");
            if (toSlot != -1 && (toSlot < 1 || toSlot > to.getSlots()))
                return MethodResult.of(null, "To slot is incorrect");

            if (limit <= 0)
                return MethodResult.of(0);
            return MethodResult.of(InventoryUtil.moveItem(new UnlimitedItemHandler(), 0, to, toSlot - 1, limit));
        }, null);
    }
}
