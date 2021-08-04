package site.siredvin.progressiveperipherals.extra.network.api;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.base.ITrickedTileEntity;
import site.siredvin.progressiveperipherals.extra.network.NetworkElementData;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public interface IEnderwireElement<T extends TileEntity & IEnderwireElement<T>> extends ITrickedTileEntity<T> {
    @Nullable String getAttachedNetwork();
    void setAttachedNetwork(@Nullable String name);
    UUID getElementUUID();
    EnderwireElementType getElementType();
    Map<String, Object> getCurrentState();

    default void changeAttachedNetwork(@Nullable String newNetwork) {
        World world = getWorld();
        if (world != null && !world.isClientSide) {
            String oldNetwork = getAttachedNetwork();
            NetworkElementTool.changeAttachedNetwork(oldNetwork, newNetwork, this, (ServerWorld) world);
        }
    }

    default NetworkElementData generateElementData() {
        return new NetworkElementData(
                getElementUUID(), getPosition(), getElementType().getCategory(), getElementType().name().toLowerCase(),
                Objects.requireNonNull(getWorld()).dimension().location().toString(), getAmplifier()
        );
    }

    default @NotNull NetworkAmplifier getAmplifier() {
        return NetworkAmplifier.NONE;
    }

    default MethodResult configure(Map<?, ?> data) throws LuaException {
        return MethodResult.of(null, "This element are not configurable");
    }
}
