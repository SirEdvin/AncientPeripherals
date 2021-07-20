package site.siredvin.progressiveperipherals.api.tileentity;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ITileEntityStackContainer {
    @NotNull ItemStack getStoredStack();
    void setStoredStack(@NotNull ItemStack stack);
    boolean hasStoredStack();
}
