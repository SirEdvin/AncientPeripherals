package site.siredvin.progressiveperipherals.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.computercraft.peripherals.abstractions.ExperienceAutomataCorePeripheral;
import site.siredvin.progressiveperipherals.utils.CheckUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class EnchantingAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {
    private static final String ENCHANT_OPERATION = "enchant";
    private static final int MINECRAFT_ENCHANTING_LEVEL_LIMIT = 30;

    public EnchantingAutomataCorePeripheral(String type, ITurtleAccess turtle, TurtleSide side) {
        super(type, turtle, side);
    }

    @Override
    protected int getRawCooldown(String s) {
        if (s.equals(ENCHANT_OPERATION))
            return ProgressivePeripheralsConfig.enchantCooldown;
        return super.getRawCooldown(s);
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("enchantCost", ProgressivePeripheralsConfig.enchantCost);
        data.put("enchantCooldown", ProgressivePeripheralsConfig.enchantCooldown);
        data.put("enchantLevelCost", ProgressivePeripheralsConfig.enchantLevelCost);
        data.put("treasureEnchantmentsAllowed", allowTreasureEnchants());
        data.put("enchantmentWipeChance", ProgressivePeripheralsConfig.enchantingAutomataCoreDisappearChance);
        return data;
    }

    @Override
    public int getInteractionRadius() {
        return ProgressivePeripheralsConfig.enchantingAutomataCoreInteractionRadius;
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return ProgressivePeripheralsConfig.enchantingAutomataCoreMaxFuelConsumptionLevel;
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableEnchantingAutomataCore;
    }

    public boolean allowTreasureEnchants() {
        return false;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult enchant(int levels) {
        Optional<MethodResult> checkResults = cooldownCheck(ENCHANT_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        checkResults = consumeFuelOp(ProgressivePeripheralsConfig.enchantCost);
        if (checkResults.isPresent()) return checkResults.get();
        if (levels > MINECRAFT_ENCHANTING_LEVEL_LIMIT)
            return MethodResult.of(null, String.format("Enchanting levels cannot be bigger then %d", MINECRAFT_ENCHANTING_LEVEL_LIMIT));
        addRotationCycle();
        int requiredXP = levels * ProgressivePeripheralsConfig.enchantLevelCost;
        if (requiredXP > _getStoredXP())
            return MethodResult.of(null, String.format("Not enough XP, %d required", requiredXP));
        int selectedSlot = turtle.getSelectedSlot();
        IInventory turtleInventory = turtle.getInventory();
        ItemStack targetItem = turtleInventory.getItem(selectedSlot);
        if (!targetItem.isEnchantable())
            return MethodResult.of(null, "Item is not enchantable");
        if (targetItem.isEnchanted())
            return MethodResult.of(null, "Item already enchanted!");
        ItemStack enchantedItem = EnchantmentHelper.enchantItem(getWorld().random, owner.getToolInMainHand(), levels, allowTreasureEnchants());
        adjustStoredXP(-requiredXP);
        trackOperation(ENCHANT_OPERATION);
        turtleInventory.setItem(selectedSlot, enchantedItem);
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult extractEnchantment(int target) throws LuaException {
        Optional<MethodResult> checkResults = cooldownCheck(ENCHANT_OPERATION);
        if (checkResults.isPresent()) return checkResults.get();
        checkResults = consumeFuelOp(ProgressivePeripheralsConfig.enchantCost);
        if (checkResults.isPresent()) return checkResults.get();
        addRotationCycle();
        CheckUtils.isCorrectSlot(target);
        target--; // Convert to java slot
        IInventory turtleInventory = turtle.getInventory();
        int selectedSlot = turtle.getSelectedSlot();
        ItemStack selectedItem = turtleInventory.getItem(selectedSlot);
        ItemStack targetItem = turtleInventory.getItem(target);
        if (!selectedItem.isEnchanted())
            return MethodResult.of(null, "Selected item is not enchanted");
        if (!targetItem.getItem().equals(Items.BOOK))
            return MethodResult.of(null, "Target item is not book");
        if (targetItem.getCount() != 1)
            return MethodResult.of(null, "Target book should be 1 in stack");
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(selectedItem);
        if (getWorld().random.nextInt(100) < ProgressivePeripheralsConfig.enchantingAutomataCoreDisappearChance * 100) {
            enchants.keySet().stream().findAny().ifPresent(enchants::remove);
        }
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(enchants, enchantedBook);
        EnchantmentHelper.setEnchantments(Collections.emptyMap(), selectedItem);
        turtleInventory.setItem(target, enchantedBook);
        trackOperation(ENCHANT_OPERATION);
        return MethodResult.of(true);
    }
}
