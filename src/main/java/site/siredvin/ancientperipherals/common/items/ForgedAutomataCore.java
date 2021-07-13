package site.siredvin.ancientperipherals.common.items;

import de.srendi.advancedperipherals.api.metaphysics.IFeedableAutomataCore;
import de.srendi.advancedperipherals.common.setup.Villagers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.FakePlayer;
import site.siredvin.ancientperipherals.common.setup.Items;

import java.util.HashMap;
import java.util.Map;

public class ForgedAutomataCore extends BaseItem implements IFeedableAutomataCore {

    private static final Map<VillagerProfession, Item> FORGED_SOUL_MAPPING = new HashMap<>();

    public static void addForgedSoulRecipe(VillagerProfession profession, Item soul) {
        FORGED_SOUL_MAPPING.put(profession, soul);
    }

    public ForgedAutomataCore() {
        this(new Item.Properties().stacksTo(1));
    }

    public ForgedAutomataCore(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(player instanceof FakePlayer)) {
            player.displayClientMessage(new TranslationTextComponent("text.advancedperipherals.automata_core_feed_by_player"), true);
            return ActionResultType.FAIL;
        }
        if (entity instanceof VillagerEntity) {
            VillagerProfession profession = ((VillagerEntity) entity).getVillagerData().getProfession();
            if (FORGED_SOUL_MAPPING.containsKey(profession)) {
                player.setItemInHand(hand, new ItemStack(FORGED_SOUL_MAPPING.get(profession)));
                entity.remove();
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
