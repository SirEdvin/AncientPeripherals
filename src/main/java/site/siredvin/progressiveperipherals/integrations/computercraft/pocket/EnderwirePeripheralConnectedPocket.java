package site.siredvin.progressiveperipherals.integrations.computercraft.pocket;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePocket;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwirePocketModemPeripheral;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class EnderwirePeripheralConnectedPocket extends BasePocket<EnderwirePocketModemPeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, "enderwire_peripheral_connected_pocket");

    public EnderwirePeripheralConnectedPocket() {
        super(ID, TranslationUtil.pocket("enderwire_peripheral_connected"), Blocks.ENDERWIRE_PERIPHERAL_CONNECTOR);
    }

    @Override
    protected EnderwirePocketModemPeripheral getPeripheral(IPocketAccess pocket) {
        return new EnderwirePocketModemPeripheral(pocket);
    }

    @Override
    public void update(@NotNull IPocketAccess access, @Nullable IPeripheral peripheral) {
        if (peripheral instanceof EnderwirePocketModemPeripheral) {
            if (!((EnderwirePocketModemPeripheral) peripheral).isInitialized())
                ((EnderwirePocketModemPeripheral) peripheral).initialize();
        }
    }
}
