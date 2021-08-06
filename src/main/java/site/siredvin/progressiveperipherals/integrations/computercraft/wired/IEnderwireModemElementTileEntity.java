package site.siredvin.progressiveperipherals.integrations.computercraft.wired;

import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.base.ITrickedTileEntity;

public interface IEnderwireModemElementTileEntity<T extends TileEntity & ITrickedTileEntity<T>> extends ITrickedTileEntity<T> {
    default @Nullable EnderwireModemPeripheral getWiredPeripheral() {
        return null;
    }
}
