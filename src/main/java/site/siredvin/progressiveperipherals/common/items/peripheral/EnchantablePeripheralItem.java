package site.siredvin.progressiveperipherals.common.items.peripheral;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public class EnchantablePeripheralItem extends PeripheralItem {
    private final Set<Enchantment> allowedEnchantments;

    public EnchantablePeripheralItem(Supplier<Boolean> enabledSup, Set<Enchantment> allowedEnchantments, @Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID) {
        super(enabledSup, turtleID, pocketID);
        this.allowedEnchantments = allowedEnchantments;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return !allowedEnchantments.isEmpty();
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getEnchantments(book).keySet().stream().anyMatch(allowedEnchantments::contains) || super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return allowedEnchantments.contains(enchantment) || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 3;
    }
}
