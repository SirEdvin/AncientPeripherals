package site.siredvin.progressiveperipherals.extra.network.tools;

import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.api.tileentity.IBlockObservingTileEntity;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.EnderwireNetworkManagementPocket;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class NetworkElementTool {

    @SuppressWarnings("UnusedReturnValue")
    public static @Nullable IEnderwireNetworkElement removeFromNetwork(@NotNull String networkName, @NotNull IEnderwireElement element, @NotNull ServerWorld world) {
        GlobalNetworksData data = GlobalNetworksData.get(world);
        IEnderwireNetworkElement result = removeFromNetwork(data, networkName, element);
        data.setDirty();
        return result;
    }

    private static @Nullable IEnderwireNetworkElement removeFromNetwork(@NotNull GlobalNetworksData globalData, @NotNull String networkName, @NotNull IEnderwireElement element) {
        EnderwireNetwork network = globalData.getNetwork(networkName);
        if (network != null) {
            IEnderwireNetworkElement elementData = network.getElement(element.getElementName());
            if (elementData == null) {
                ProgressivePeripherals.LOGGER.error(String.format("Element %s is not in network %s, this shouldn't happened!", element.getElementName(), networkName));
            } else {
                network.removeNetworkElement(elementData);
                element.setElementName(null);
                return elementData;
            }
        } else {
            ProgressivePeripherals.LOGGER.error(String.format("Missing network %s, this shouldn't happened!", networkName));
        }
        return null;
    }

    public static IEnderwireNetworkElement generateElementData(@NotNull IEnderwireElement element, @NotNull EnderwireNetwork network) {
        String newElementName = network.generateNameForElement(element);
        return element.generateElementData(newElementName);
    }

    public static void connectToNewNetwork(@NotNull IEnderwireElement element, @NotNull EnderwireNetwork network, @NotNull ServerWorld world) {
        GlobalNetworksData data = GlobalNetworksData.get(world);
        connectToNewNetwork(null, element, network);
        data.setDirty();
    }

    private static void connectToNewNetwork(@Nullable IEnderwireNetworkElement elementData, @NotNull IEnderwireElement element, @NotNull EnderwireNetwork network) {
        if (elementData == null)
            elementData = generateElementData(element, network);
        element.setElementName(elementData.getName());
        network.addNetworkElement(elementData);
    }

    public static void changeAttachedNetwork(@Nullable String oldNetwork, @Nullable String newNetwork, @NotNull IEnderwireElement element, @NotNull ServerWorld world) {
        GlobalNetworksData globalData = GlobalNetworksData.get(world);
        boolean dirtyGlobalData = false;
        IEnderwireNetworkElement elementData = null;
        element.beforeAttachedNetworkChange(oldNetwork, newNetwork);
        if (oldNetwork != null) {
            elementData = removeFromNetwork(globalData, oldNetwork, element);
            if (elementData != null)
                dirtyGlobalData = true;
        }
        if (newNetwork != null) {
            EnderwireNetwork network = globalData.getNetwork(newNetwork);
            if (network == null)
                throw new IllegalArgumentException(String.format("Cannot find new network %s", newNetwork));
            connectToNewNetwork(elementData, element, network);
            dirtyGlobalData = true;
        }
        element.setAttachedNetwork(newNetwork);
        element.afterAttachedNetworkChange(oldNetwork, newNetwork);
        if (dirtyGlobalData)
            globalData.setDirty();
    }

    public static boolean isNetworkManager(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemPocketComputer && ItemPocketComputer.getUpgrade(stack) instanceof EnderwireNetworkManagementPocket;
    }

    public static void handleNetworkSetup(Hand playerHand, PlayerEntity player, World world, BlockPos pos) {
        ItemStack itemInHand = player.getItemInHand(playerHand);
        if (isNetworkManager(itemInHand)) {
            IEnderwireElement te = (IEnderwireElement) world.getBlockEntity(pos);
            if (te != null) {
                if (te.getElementType().isEnabled()) {
                    if (!world.isClientSide) {
                        EnderwireNetwork selectedNetwork = NetworkAccessingTool.getSelectedNetwork(GlobalNetworksData.get((ServerWorld) world), ItemPocketComputer.getUpgradeInfo(itemInHand));
                        if (selectedNetwork != null) {
                            if (!selectedNetwork.canAcceptNewElements(player)) {
                                player.displayClientMessage(TranslationUtil.formattedLocalization("enderwire.network_limit", TextFormatting.DARK_GRAY, selectedNetwork.getName()), true);
                            } else {
                                te.changeAttachedNetwork(selectedNetwork.getName());
                            }
                        } else {
                            te.changeAttachedNetwork(null);
                        }
                    }
                } else if (world.isClientSide) {
                    player.displayClientMessage(TranslationUtil.localization("enderwire.is_disabled", TextFormatting.DARK_GRAY), true);
                }
            }
        }
    }

    public static void handleNetworkDisplay(PlayerEntity player, World world, BlockPos pos, Hand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (isNetworkManager(itemInHand)) {
            IEnderwireElement te = (IEnderwireElement) world.getBlockEntity(pos);
            if (te != null) {
                String attachedNetwork = te.getAttachedNetwork();
                if (!te.getElementType().isEnabled())
                    player.displayClientMessage(TranslationUtil.localization("enderwire.is_disabled", TextFormatting.DARK_GRAY), true);
                if (attachedNetwork == null) {
                    player.displayClientMessage(
                            TranslationUtil.localization("enderwire.not_attached_to_anything", TextFormatting.DARK_GRAY),
                            true
                    );
                } else {
                    player.displayClientMessage(
                            TranslationUtil.formattedLocalization("enderwire.attached_to", TextFormatting.DARK_GRAY, attachedNetwork, te.getElementName()),
                            true
                    );
                }
            }
        }
    }

    public static @Nullable ActionResultType handleUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockRayTraceResult hit) {
        ItemStack mainHandItem = player.getMainHandItem();
        if (!mainHandItem.isEmpty() && hand == Hand.MAIN_HAND && isNetworkManager(mainHandItem)) {
            handleNetworkSetup(hand, player, world, pos);
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
            IEnderwireElement te = (IEnderwireElement) world.getBlockEntity(pos);
            if (te != null && te.getAttachedNetwork() != null) {
                if (te instanceof IBlockObservingTileEntity)
                    ((IBlockObservingTileEntity) te).destroy();
                removeFromNetwork(te.getAttachedNetwork(), te, (ServerWorld) world);
            }
        }
    }
}
