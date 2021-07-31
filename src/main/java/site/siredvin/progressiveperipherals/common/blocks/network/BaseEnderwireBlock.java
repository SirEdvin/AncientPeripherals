package site.siredvin.progressiveperipherals.common.blocks.network;

import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.enderwire.IEnderwireElement;
import site.siredvin.progressiveperipherals.common.blocks.base.TileEntityBlock;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.NetworkAccessingTool;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.NetworkManagementPocket;

public abstract class BaseEnderwireBlock<T extends TileEntity & IEnderwireElement> extends TileEntityBlock<T> {

    public BaseEnderwireBlock(Properties properties) {
        super(properties);
    }

    protected boolean isSuitableItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemPocketComputer && ItemPocketComputer.getUpgrade(stack) instanceof NetworkManagementPocket;
    }

    protected boolean processNetworkSetup(Hand playerHand, PlayerEntity player, ServerWorld world, BlockPos pos) {
        ItemStack itemInHand = player.getItemInHand(playerHand);
        if (isSuitableItem(itemInHand)) {
            NetworkData selectedNetwork = NetworkAccessingTool.getSelectedNetwork(GlobalNetworksData.get(world), ItemPocketComputer.getUpgradeInfo(itemInHand));
            if (selectedNetwork != null) {
                IEnderwireElement te = (IEnderwireElement) world.getBlockEntity(pos);
                if (te != null) {
                    te.setAttachedNetwork(selectedNetwork.getName());
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean processNetworkDisplay(PlayerEntity player, World world, BlockPos pos, Hand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (isSuitableItem(itemInHand)) {
            IEnderwireElement te = (IEnderwireElement) world.getBlockEntity(pos);
            if (te != null) {
                String attachedNetwork = te.getAttachedNetwork();
                if (attachedNetwork == null) {
                    player.displayClientMessage(new StringTextComponent("This element don't attached to any network"), true);
                } else {
                    player.displayClientMessage(new StringTextComponent(String.format("This element attached to %s", attachedNetwork)), true);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void setPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            processNetworkSetup(Hand.OFF_HAND, (PlayerEntity) entity, (ServerWorld) world, pos);
        }
    }

    @Override
    public @NotNull ActionResultType use(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity playerEntity, @NotNull Hand hand, @NotNull BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            switch (hand) {
                case MAIN_HAND:
                    if (processNetworkSetup(hand, playerEntity, (ServerWorld) world, pos))
                        return ActionResultType.SUCCESS;
                case OFF_HAND:
                    if (processNetworkDisplay(playerEntity, world, pos, hand))
                        return ActionResultType.SUCCESS;
            }
        }
        return super.use(state, world, pos, playerEntity, hand, hit);
    }
}
