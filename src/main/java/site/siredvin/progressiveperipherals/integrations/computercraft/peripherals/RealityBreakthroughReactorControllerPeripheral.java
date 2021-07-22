package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class RealityBreakthroughReactorControllerPeripheral extends BasePeripheral {
    private final RealityBreakthroughRectorControllerTileEntity tileEntity;
    public RealityBreakthroughReactorControllerPeripheral(String type, RealityBreakthroughRectorControllerTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction
    public final boolean isConnected() {
        return tileEntity.isConfigured();
    }

    @LuaFunction(mainThread = true)
    public final MethodResult connect() {
        Pair<Boolean, String> result = tileEntity.detectMultiBlock();
        return MethodResult.of(result.getLeft(), result.getRight());
    }
}
