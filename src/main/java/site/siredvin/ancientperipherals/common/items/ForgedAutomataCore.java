package site.siredvin.ancientperipherals.common.items;

import de.srendi.advancedperipherals.api.metaphysics.IFeedableAutomataCore;
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

import java.util.function.Supplier;

public class ForgedAutomataCore extends BaseItem implements IFeedableAutomataCore {

    public ForgedAutomataCore() {
        this(new Item.Properties().stacksTo(1));
    }

    public ForgedAutomataCore(Properties properties) {
        super(properties);
    }

    public ForgedAutomataCore(Properties properties, Supplier<Boolean> enabledSup) {
        super(properties, enabledSup);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(player instanceof FakePlayer)) {
            player.displayClientMessage(new TranslationTextComponent("text.advancedperipherals.automata_core_feed_by_player"), true);
            return ActionResultType.FAIL;
        }
        if (entity instanceof VillagerEntity && ((VillagerEntity) entity).getVillagerData().getProfession().equals(VillagerProfession.LIBRARIAN)) {
            player.setItemInHand(hand, new ItemStack(Items.TRAINABLE_AUTOMATA_CORE.get()));
        }
        return ActionResultType.PASS;
    }
}
