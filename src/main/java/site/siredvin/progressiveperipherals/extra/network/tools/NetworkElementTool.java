package site.siredvin.progressiveperipherals.extra.network.tools;

import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.block.BlockState;
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
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.EnderwireNetworkManagementPocket;

public class NetworkElementTool {

    public static <T extends TileEntity & IEnderwireElement<T>> @Nullable EnderwireNetworkElement removeFromNetwork(@NotNull GlobalNetworksData globalData, @NotNull String networkName, @NotNull IEnderwireElement<T> element, @NotNull ServerWorld world) {
        EnderwireNetwork network = globalData.getNetwork(networkName);
        if (network != null) {
            EnderwireNetworkElement elementData = network.getElement(element.getElementUUID());
            if (elementData == null) {
                ProgressivePeripherals.LOGGER.error(String.format("Element %s is not in network %s, this shouldn't happened!", element.getElementUUID(), networkName));
            } else {
                network.removeNetworkElement(elementData);
                return elementData;
            }
        } else {
            ProgressivePeripherals.LOGGER.error(String.format("Missing network %s, this shouldn't happened!", networkName));
        }
        return null;
    }

    public static <T extends TileEntity & IEnderwireElement<T>> void changeAttachedNetwork(@Nullable String oldNetwork, @Nullable String newNetwork, @NotNull IEnderwireElement<T> element, @NotNull ServerWorld world) {
        GlobalNetworksData globalData = GlobalNetworksData.get(world);
        boolean dirtyGlobalData = false;
        EnderwireNetworkElement elementData = null;
        if (oldNetwork != null) {
            elementData = removeFromNetwork(globalData, oldNetwork, element, world);
            if (elementData != null)
                dirtyGlobalData = true;
        }
        if (newNetwork != null) {
            EnderwireNetwork network = globalData.getNetwork(newNetwork);
            if (network == null)
                throw new IllegalArgumentException(String.format("Cannot find new network %s", newNetwork));
            if (elementData == null)
                elementData = element.generateElementData();
            network.addNetworkElement(elementData);
            dirtyGlobalData = true;
        }
        element.setAttachedNetwork(newNetwork);
        if (dirtyGlobalData)
            globalData.setDirty();
    }

    public static boolean isNetworkManager(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemPocketComputer && ItemPocketComputer.getUpgrade(stack) instanceof EnderwireNetworkManagementPocket;
    }

    public static void handleNetworkSetup(Hand playerHand, PlayerEntity player, ServerWorld world, BlockPos pos) {
        ItemStack itemInHand = player.getItemInHand(playerHand);
        if (isNetworkManager(itemInHand)) {
            EnderwireNetwork selectedNetwork = NetworkAccessingTool.getSelectedNetwork(GlobalNetworksData.get(world), ItemPocketComputer.getUpgradeInfo(itemInHand));
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null) {
                if (selectedNetwork != null) {
                    te.changeAttachedNetwork(selectedNetwork.getName());
                } else {
                    te.changeAttachedNetwork(null);
                }
            }
        }
    }

    public static void handleNetworkDisplay(PlayerEntity player, World world, BlockPos pos, Hand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (isNetworkManager(itemInHand)) {
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null) {
                String attachedNetwork = te.getAttachedNetwork();
                if (attachedNetwork == null) {
                    player.displayClientMessage(
                            new StringTextComponent(
                                    String.format("This element with UUID %s don't attached to any network", te.getElementUUID())
                            ),
                            true
                    );
                } else {
                    player.displayClientMessage(
                            new StringTextComponent(
                                    String.format("This element attached to %s, with UUID %s", attachedNetwork, te.getElementUUID())
                            ),
                            true
                    );
                }
            }
        }
    }

    public static @Nullable ActionResultType handleUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockRayTraceResult hit) {
        ItemStack mainHandItem = player.getMainHandItem();
        if (!mainHandItem.isEmpty() && hand == Hand.MAIN_HAND && isNetworkManager(mainHandItem)) {
            if (!world.isClientSide)
                handleNetworkSetup(hand, player, (ServerWorld) world, pos);
            return ActionResultType.SUCCESS;
        } else {
            ItemStack offhandItem = player.getOffhandItem();
            if (isNetworkManager(offhandItem)) {
                if (world.isClientSide)
                    handleNetworkDisplay(player, world, pos, Hand.OFF_HAND);
                return ActionResultType.SUCCESS;
            }
        }
        return null;
    }

    public static void handleRemove(@NotNull World world, @NotNull BlockPos pos) {
        if (!world.isClientSide) {
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null && te.getAttachedNetwork() != null) {
                ServerWorld serverWorld = (ServerWorld) world;
                removeFromNetwork(GlobalNetworksData.get(serverWorld), te.getAttachedNetwork(), te, serverWorld);
            }
        }
    }
}
