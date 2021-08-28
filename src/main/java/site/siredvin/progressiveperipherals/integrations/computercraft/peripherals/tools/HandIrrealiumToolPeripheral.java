package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.tools;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.actions.TurtlePlaceAction;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.UltimineMode;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.dataproxy.IrrealiumToolDataProxy;

import java.util.Collection;

public class HandIrrealiumToolPeripheral extends BaseIrrealiumToolPeripheral {

    private final ITurtleAccess turtle;
    private final TurtleSide side;

    public HandIrrealiumToolPeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(turtle, side);
        this.turtle = turtle;
        this.side = side;
    }

    protected MethodResult place(Direction direction) {
        UltimineMode mode = IrrealiumToolDataProxy.getUltimineMode(owner);

        BlockPos center = turtle.getPosition().relative(direction);
        Collection<BlockPos> targets = mode.getTargetArea(direction, turtle.getDirection(), center);
        if (!turtle.consumeFuel(IrrealiumToolDataProxy.getOperationType(owner).getCost(targets.size(), 0, 0)))
            return MethodResult.of(null, "Not enough fuel");
        MethodResult result = TurtlePlaceAction.place(turtle, targets, direction);
        turtle.playAnimation(side == TurtleSide.LEFT ? TurtleAnimation.SWING_LEFT_TOOL : TurtleAnimation.SWING_RIGHT_TOOL);
        return result;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult place() {
        return place(owner.getFacing());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult placeUp() {
        return place(Direction.UP);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult placeDown() {
        return place(Direction.DOWN);
    }
}
