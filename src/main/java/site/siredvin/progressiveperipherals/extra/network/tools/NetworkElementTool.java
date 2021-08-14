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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.integrations.computercraft.pocket.EnderwireNetworkManagementPocket;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.Objects;

public class NetworkElementTool {

    public static <T extends TileEntity & IEnderwireElement<T>> @Nullable EnderwireNetworkElement removeFromNetwork(@NotNull GlobalNetworksData globalData, @NotNull String networkName, @NotNull IEnderwireElement<T> element, @NotNull ServerWorld world) {
        EnderwireNetwork network = globalData.getNetwork(networkName);
        if (network != null) {
            EnderwireNetworkElement elementData = network.getElement(element.getElementName());
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

    public static <T extends TileEntity & IEnderwireElement<T>> EnderwireNetworkElement generateElementData(@NotNull IEnderwireElement<T> element, @NotNull EnderwireNetwork network) {
        String newElementName = network.generateNameForElement(element);
        return new EnderwireNetworkElement(
                newElementName, element.getPosition(), element.getElementType().getCategory(), element.getElementType(),
                Objects.requireNonNull(element.getWorld()).dimension().location().toString(), element.getAmplifier()
        );
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
                elementData = generateElementData(element, network);
            element.setElementName(elementData.getName());
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

    public static void handleNetworkSetup(Hand playerHand, PlayerEntity player, World world, BlockPos pos) {
        ItemStack itemInHand = player.getItemInHand(playerHand);
        if (isNetworkManager(itemInHand)) {
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null) {
                if (te.getElementType().isEnabled()) {
                    if (!world.isClientSide) {
                        EnderwireNetwork selectedNetwork = NetworkAccessingTool.getSelectedNetwork(GlobalNetworksData.get((ServerWorld) world), ItemPocketComputer.getUpgradeInfo(itemInHand));
                        if (selectedNetwork != null) {
                            if (!selectedNetwork.canAcceptNewElements()) {
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
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
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
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null && te.getAttachedNetwork() != null) {
                ServerWorld serverWorld = (ServerWorld) world;
                removeFromNetwork(GlobalNetworksData.get(serverWorld), te.getAttachedNetwork(), te, serverWorld);
            }
        }
    }
}
