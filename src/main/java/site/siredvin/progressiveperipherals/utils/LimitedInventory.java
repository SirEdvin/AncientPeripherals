package site.siredvin.progressiveperipherals.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LimitedInventory implements IInventory {
    private final int[] usedSlots;
    private final IInventory parent;

    public LimitedInventory(IInventory parent, int[] usedSlots) {
        this.usedSlots = usedSlots;
        this.parent = parent;
    }
    @Override
    public int getContainerSize() {
        return usedSlots.length;
    }

    @Override
    public boolean isEmpty() {
        for (int slot: usedSlots) {
            if (!parent.getItem(slot).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return parent.getItem(usedSlots[slot]);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int p_70298_2_) {
        return parent.removeItem(usedSlots[slot], p_70298_2_);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return parent.removeItemNoUpdate(usedSlots[slot]);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack item) {
        parent.setItem(usedSlots[slot], item);
    }

    @Override
    public void setChanged() {
        parent.setChanged();
    }

    public void reduceCount(int slot) {
        reduceCount(slot, 1);
    }

    public void reduceCount(int slot, int limit) {
        ItemStack item = parent.getItem(usedSlots[slot]);
        int itemCount = item.getCount();
        if (itemCount <= limit) {
            parent.setItem(usedSlots[slot], ItemStack.EMPTY);
        } else {
            item.setCount(item.getCount() - limit);
            parent.setItem(usedSlots[slot], item);
        }
    }

    @Override
    public boolean stillValid(@NotNull PlayerEntity player) {
        return parent.stillValid(player);
    }

    @Override
    public void clearContent() {
        for (int slot: usedSlots)
            parent.setItem(slot, ItemStack.EMPTY);
    }
}
