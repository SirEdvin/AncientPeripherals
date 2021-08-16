package site.siredvin.progressiveperipherals.extra.network.api;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.base.ITrickedTileEntity;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

import java.util.Map;

public interface IEnderwireElement<T extends TileEntity & IEnderwireElement<T>> extends ITrickedTileEntity<T> {
    @Nullable String getAttachedNetwork();
    void setAttachedNetwork(@Nullable String name);
    @Nullable String getElementName();
    void setElementName(@Nullable String name);
    EnderwireElementType getElementType();
    Map<String, Object> getCurrentState();

    default void changeAttachedNetwork(@Nullable String newNetwork) {
        World world = getWorld();
        if (world != null && !world.isClientSide) {
            String oldNetwork = getAttachedNetwork();
            NetworkElementTool.changeAttachedNetwork(oldNetwork, newNetwork, this, (ServerWorld) world);
        }
    }

    default void beforeAttachedNetworkChange(String oldNetwork, String newNetwork) {

    }

    default void afterAttachedNetworkChange(String oldNetwork, String newNetwork) {

    }

    default @NotNull NetworkAmplifier getAmplifier() {
        return NetworkAmplifier.NONE;
    }

    default MethodResult configure(Map<?, ?> data) throws LuaException {
        return MethodResult.of(null, "This element are not configurable");
    }

    default @Nullable IPeripheral getSharedPeripheral() {
        if (getElementType().getCategory().canSharePeripheral())
            throw new IllegalArgumentException("You need to overwrite this function ...");
        throw new IllegalArgumentException("You should can this function for this element");
    }
}
