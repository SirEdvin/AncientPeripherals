package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.UltimineMode;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.dataproxy.IrrealiumToolDataProxy;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.HashMap;
import java.util.Map;

public class IrrealiumToolPeripheral extends BasePeripheral {
    public static final String TYPE = "irrealiumTool";

    public IrrealiumToolPeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(TYPE, turtle, side);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableIrrealiumTools;
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
    public final MethodResult getUltimineMode() {
        UltimineMode mode = IrrealiumToolDataProxy.getUltimineMode(owner);
        return MethodResult.of(mode.prettyName(), mode.getDescription());
    }

    @LuaFunction
    public final Map<Integer, Map<String, String>> getUltimineModes() {
        UltimineMode[] values = UltimineMode.values();
        Map<Integer, Map<String, String>> data = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            Map<String, String> description = new HashMap<>();
            description.put("name", values[i].prettyName());
            description.put("description", values[i].getDescription());
            data.put(i + 1, description);
        }
        return data;
    }

    @LuaFunction
    public final MethodResult setUltimineMode(String mode) {
        try {
            IrrealiumToolDataProxy.setUltimineMod(owner, UltimineMode.fromPretty(mode));
            return MethodResult.of(true);
        } catch (IllegalArgumentException ignored) {}
        return MethodResult.of(null, "Incorrect mode provided");
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

    @LuaFunction
    public final Map<String, Object> getFuelConsumptionInformation() {
        return IrrealiumToolDataProxy.getOperationType(owner).computerDescription();
    }
}
