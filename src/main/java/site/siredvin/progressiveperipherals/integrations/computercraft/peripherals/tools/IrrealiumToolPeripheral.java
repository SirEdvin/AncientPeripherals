package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.tools;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.dataproxy.IrrealiumToolDataProxy;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.Map;

public class IrrealiumToolPeripheral extends BaseIrrealiumToolPeripheral {

    public IrrealiumToolPeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(turtle, side);
    }

    @LuaFunction
    public final boolean isVoiding() {
        return IrrealiumToolDataProxy.isVoiding(owner);
    }

    @LuaFunction
    public final boolean setVoiding(boolean voiding) {
        IrrealiumToolDataProxy.setVoiding(owner, voiding);
        return true;
    }

    @LuaFunction
    public final Map<Integer, String> getVoidingTags() {
        return LuaUtils.toLua(IrrealiumToolDataProxy.getVoidingTags(owner));
    }

    @LuaFunction
    public final boolean addVoidingTag(String tag) {
        IrrealiumToolDataProxy.addToVoidingTags(owner, tag);
        return true;
    }

    @LuaFunction
    public final boolean removeVoidingTag(String tag) {
        IrrealiumToolDataProxy.removeFromVoidingTags(owner, tag);
        return true;
    }

    @LuaFunction
    public final boolean clearVoidingTags() {
        IrrealiumToolDataProxy.clearVoidingTags(owner);
        return true;
    }
}
