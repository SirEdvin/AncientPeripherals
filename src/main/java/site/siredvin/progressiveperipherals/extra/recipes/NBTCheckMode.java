package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import site.siredvin.progressiveperipherals.utils.NBTUtils;

public enum NBTCheckMode {
    FULL, SUBSET, SUPERSET;

    public boolean itemStackEquals(ItemStack result, ItemStack targetingResult) {
        if (result.getItem() != targetingResult.getItem())
            return false;
        CompoundNBT targetingNBT = targetingResult.getTag();
        CompoundNBT resultNBT = result.getTag();
        if (targetingNBT == null)
            return resultNBT == null || this == NBTCheckMode.SUBSET;
        if (resultNBT == null)
            return this == NBTCheckMode.SUPERSET;
        if (this == NBTCheckMode.FULL)
            return resultNBT.equals(targetingNBT);
        if (this == NBTCheckMode.SUBSET)
            return NBTUtils.isSubSet(resultNBT, targetingNBT);
        return NBTUtils.isSubSet(targetingNBT, resultNBT);
    }
}
