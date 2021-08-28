package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.actions;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.shared.TurtlePermissions;
import de.srendi.advancedperipherals.common.util.fakeplayer.APFakePlayer;
import de.srendi.advancedperipherals.common.util.fakeplayer.FakePlayerProviderTurtle;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.utils.ValueContainer;

import java.util.Collection;

public class TurtlePlaceAction {
    /*
    Just ugly rework of https://github.com/SquidDev-CC/CC-Tweaked/blob/mc-1.16.x/src/main/java/dan200/computercraft/shared/turtle/core/TurtlePlaceCommand.java
     */

    @NotNull
    public static MethodResult place(@NotNull ITurtleAccess turtle, @NotNull Collection<BlockPos> targets, @NotNull Direction direction) {
        ItemStack stack = turtle.getInventory().getItem(turtle.getSelectedSlot());
        if (stack.isEmpty())
            return MethodResult.of(null, "No items to place");
        if (!(stack.getItem() instanceof BlockItem))
            return MethodResult.of(null, "Target item should be blocks");
        World world = turtle.getWorld();
        return FakePlayerProviderTurtle.withPlayer(turtle, player -> {
            BlockPos turtlePos = turtle.getPosition();
            player.teleportTo(turtlePos.getX(), turtlePos.getY(), turtlePos.getZ());
            // check everything
            for (BlockPos placeTarget : targets) {
                TurtleBlockEvent.Place place = new TurtleBlockEvent.Place(turtle, player, turtle.getWorld(), placeTarget, stack);
                if (MinecraftForge.EVENT_BUS.post(place))
                    return MethodResult.of(null, place.getFailureMessage());
                if (ComputerCraft.turtlesObeyBlockProtection) {
                    if (!TurtlePermissions.isBlockEditable(world, placeTarget, player))
                        return MethodResult.of(null, "Cannot place in protected area");
                }
            }
            ValueContainer<String> errorContainer = new ValueContainer<>();
            for (BlockPos placeTarget: targets) {
                if (!deployOnBlock(turtle, player, placeTarget, direction.getOpposite(), errorContainer))
                    return MethodResult.of(null, errorContainer.getValue());
            }
            turtle.playAnimation(TurtleAnimation.WAIT);
            return MethodResult.of(true);
        });
    }
    private static boolean canDeployOnBlock(ITurtleAccess turtle, APFakePlayer player, BlockPos position, ValueContainer<String> errorMessage) {
        World world = turtle.getWorld();
        if (!world.isEmptyBlock(position)) {
            errorMessage.setValue("Target collide with another block");
            return false;
        }

        if (ComputerCraft.turtlesObeyBlockProtection) {
            if (!TurtlePermissions.isBlockEditable(world, position, player)) {
                errorMessage.setValue("Cannot place in protected area");
                return false;
            }
        }

        return true;
    }

    private static boolean deployOnBlock(ITurtleAccess turtle, APFakePlayer turtlePlayer, BlockPos position, Direction side, ValueContainer<String> errorMessage) {
        BlockRayTraceResult hit = new BlockRayTraceResult(new Vector3d(0.5f, 0.5f, 0.5f), side, position, false);
        ItemUseContext context = new ItemUseContext(turtlePlayer, Hand.MAIN_HAND, hit);
        if (!canDeployOnBlock(turtle, turtlePlayer, position, errorMessage)) {
            return false;
        }

        return ForgeHooks.onPlaceItemIntoWorld(context).consumesAction();
    }
}
