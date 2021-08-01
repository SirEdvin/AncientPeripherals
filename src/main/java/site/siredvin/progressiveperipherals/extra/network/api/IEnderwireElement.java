package site.siredvin.progressiveperipherals.extra.network.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.base.ITrickedTileEntity;
import site.siredvin.progressiveperipherals.extra.network.NetworkElementData;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

import java.util.Objects;
import java.util.UUID;

public interface IEnderwireElement<T extends TileEntity & IEnderwireElement<T>> extends ITrickedTileEntity<T> {
    @Nullable String getAttachedNetwork();
    void setAttachedNetwork(@Nullable String name);
    EnderwireElementType getElementType();

    default void changeAttachedNetwork(@Nullable String newNetwork) {
        World world = getWorld();
        if (world != null && !world.isClientSide) {
            String oldNetwork = getAttachedNetwork();
            NetworkElementTool.changeAttachedNetwork(oldNetwork, newNetwork, this, (ServerWorld) world);
        }
    }

    default NetworkElementData generateElementData() {
        return new NetworkElementData(getElementUUID(), getPosition(), getElementType(), getDeviceType(), Objects.requireNonNull(getWorld()).dimension().location().toString());
    }

    UUID getElementUUID();
    String getDeviceType();
}
