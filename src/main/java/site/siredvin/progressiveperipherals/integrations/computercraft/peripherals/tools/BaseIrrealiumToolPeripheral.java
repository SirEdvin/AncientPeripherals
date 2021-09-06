package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.tools;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import de.srendi.advancedperipherals.lib.peripherals.owner.TurtlePeripheralOwner;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.UltimineMode;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.dataproxy.IrrealiumToolDataProxy;

import java.util.HashMap;
import java.util.Map;

public class BaseIrrealiumToolPeripheral extends BasePeripheral<TurtlePeripheralOwner> {
    public static final String TYPE = "irrealiumTool";

    public BaseIrrealiumToolPeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(TYPE, new TurtlePeripheralOwner(turtle, side));
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableIrrealiumTools;
    }

    @LuaFunction(mainThread = true)
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

    @LuaFunction(mainThread = true)
    public final MethodResult setUltimineMode(String mode) {
        try {
            IrrealiumToolDataProxy.setUltimineMod(owner, UltimineMode.fromPretty(mode));
            return MethodResult.of(true);
        } catch (IllegalArgumentException ignored) {}
        return MethodResult.of(null, "Incorrect mode provided");
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getFuelConsumptionInformation() {
        return IrrealiumToolDataProxy.getOperationType(owner).computerDescription();
    }
}
