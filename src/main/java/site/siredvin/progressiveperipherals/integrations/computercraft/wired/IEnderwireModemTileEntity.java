package site.siredvin.progressiveperipherals.integrations.computercraft.wired;

import dan200.computercraft.shared.peripheral.modem.wired.WiredModemLocalPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.base.ITrickedTileEntity;

public interface IEnderwireModemTileEntity<T extends TileEntity & ITrickedTileEntity<T>> extends IEnderwireModemElementTileEntity<T> {
    @NotNull EnderwireModemElement getWiredElement();
    @NotNull Vector3d getWiredPosition();
    @NotNull WiredModemLocalPeripheral getLocalPeripheral();
}
