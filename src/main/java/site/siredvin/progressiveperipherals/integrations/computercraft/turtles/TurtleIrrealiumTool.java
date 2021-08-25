package site.siredvin.progressiveperipherals.integrations.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigTool;

import java.util.Collection;

public class TurtleIrrealiumTool extends TurtleDigTool {
    public TurtleIrrealiumTool(ResourceLocation id, String adjective, ItemStack itemStack) {
        super(id, adjective, itemStack);
    }

    @Override
    public @NotNull TurtleDigOperationType getOperationType() {
        return null;
    }

    @Override
    protected TurtleCommandResult dig(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction) {
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return false;
    }

    @Override
    protected @NotNull ItemStack getMimicTool() {
        return null;
    }

    @Override
    protected @NotNull Collection<BlockPos> detectTargetBlocks(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull Direction direction, @NotNull World world) {
        return null;
    }
}
