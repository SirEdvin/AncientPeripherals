package site.siredvin.ancientperipherals.common.items;

import de.srendi.advancedperipherals.api.metaphysics.IFeedableMechanicSoul;
import de.srendi.advancedperipherals.common.items.WeakMechanicSoul;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.FakePlayer;
import site.siredvin.ancientperipherals.common.setup.Items;

import java.util.Optional;
import java.util.function.Supplier;

public class ForgedMechanicSoul extends BaseItem implements IFeedableMechanicSoul {

    public ForgedMechanicSoul() {
        this(new Item.Properties().stacksTo(1));
    }

    public ForgedMechanicSoul(Properties properties) {
        super(properties);
    }

    public ForgedMechanicSoul(Properties properties, Supplier<Boolean> enabledSup) {
        super(properties, enabledSup);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(player instanceof FakePlayer)) {
            player.displayClientMessage(new TranslationTextComponent("text.advancedperipherals.weak_mechanical_player_used_by_player"), true);
            return ActionResultType.FAIL;
        }
        if (entity instanceof VillagerEntity && ((VillagerEntity) entity).getVillagerData().getProfession().equals(VillagerProfession.LIBRARIAN)) {
            player.setItemInHand(hand, new ItemStack(Items.TRAINABLE_MECHANIC_SOUL.get()));
        }
        return ActionResultType.PASS;
    }
}
