package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import de.srendi.advancedperipherals.common.util.InventoryUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EnderwireTurtlePeripheral implements IPeripheral {
    private final @NotNull ITurtleAccess turtle;

    public EnderwireTurtlePeripheral(@NotNull ITurtleAccess turtle) {
        this.turtle = turtle;
    }

    @NotNull
    @Override
    public String getType() {
        return "turtle";
    }

    @Override
    public boolean equals(@Nullable IPeripheral peripheral) {
        if (!(peripheral instanceof EnderwireTurtlePeripheral))
            return false;
        EnderwireTurtlePeripheral other = (EnderwireTurtlePeripheral) peripheral;
        return other.turtle.equals(this.turtle);
    }

    private @Nullable TileTurtle getTileEntity() {
        return (TileTurtle) turtle.getWorld().getBlockEntity(turtle.getPosition());
    }

    @LuaFunction
    public final MethodResult size() {
        return MethodResult.of(turtle.getInventory().getContainerSize());
    }

    @LuaFunction
    public final MethodResult list() {
        Map<Integer, Map<String, Object>> data = new HashMap<>();
        for (int slot = 0; slot < turtle.getInventory().getContainerSize(); slot++) {
            ItemStack stack = turtle.getInventory().getItem(slot);
            if (stack.isEmpty())
                continue;
            data.put(slot + 1, new HashMap<String, Object>() {{
                put("count", stack.getCount());
                ResourceLocation registryName = stack.getItem().getRegistryName();
                if (registryName != null) {
                    put("name", registryName.toString());
                } else {
                    put("name", "unknown");
                }
            }});
        }
        return MethodResult.of(data);
    }

    @LuaFunction
    public final MethodResult getItemDetail(int slot) {
        int javaSlot = slot - 1;
        if (javaSlot >= turtle.getInventory().getContainerSize())
            return MethodResult.of(null, "Slot is out of range");
        ItemStack stack = turtle.getInventory().getItem(javaSlot);
        if (stack.isEmpty())
            return MethodResult.of(new HashMap<>());
        Map<String, Object> data = new HashMap<>();
        data.put("count", stack.getCount());
        data.put("displayName", stack.getDisplayName().getString());
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName != null) {
            data.put("name", registryName.toString());
        } else {
            data.put("name", "unknown");
        }
        data.put("tags", stack.getItem().getTags());
        data.put("maxCount", stack.getItem().getItemStackLimit(stack));
        return MethodResult.of(data);
    }

    @LuaFunction
    public final MethodResult getItemLimit(int slot) {
        int javaSlot = slot - 1;
        if (javaSlot >= turtle.getInventory().getContainerSize())
            return MethodResult.of(null, "Slot is out of range");
        ItemStack stack = turtle.getInventory().getItem(javaSlot);
        return MethodResult.of(stack.getItem().getItemStackLimit(stack));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult pushItems(@NotNull IComputerAccess access, @NotNull IArguments arguments) throws LuaException {
        return InventoryUtil.pushItems(arguments, access, turtle.getItemHandler());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult pullItems(@NotNull IComputerAccess access, @NotNull IArguments arguments) throws LuaException {
        return InventoryUtil.pullItems(arguments, access, turtle.getItemHandler());
    }

    @LuaFunction
    public final void turnOn() {
        TileTurtle tileEntity = getTileEntity();
        if (tileEntity != null)
            tileEntity.getServerComputer().turnOn();
    }

    @LuaFunction
    public final void shutdown() {
        TileTurtle tileEntity = getTileEntity();
        if (tileEntity != null)
            tileEntity.getServerComputer().shutdown();
    }

    @LuaFunction
    public final void reboot() {
        TileTurtle tileEntity = getTileEntity();
        if (tileEntity != null)
            tileEntity.getServerComputer().reboot();
    }

    @LuaFunction
    public final int getID() {
        TileTurtle tileEntity = getTileEntity();
        if (tileEntity != null)
            return tileEntity.getServerComputer().getID();
        return -1;
    }

    @LuaFunction
    public final boolean isOn() {
        TileTurtle tileEntity = getTileEntity();
        if (tileEntity != null)
            return tileEntity.getServerComputer().isOn();
        return false;
    }

    @Nullable
    @LuaFunction
    public final String getLabel() {
        TileTurtle tileEntity = getTileEntity();
        if (tileEntity != null)
            return tileEntity.getServerComputer().getLabel();
        return "";
    }
}
