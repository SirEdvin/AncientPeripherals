package site.siredvin.progressiveperipherals.integrations.computercraft.pocket;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePocket;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireUpgradeModemPeripheral;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class EnderwirePeripheralConnectedPocket extends BasePocket<EnderwireUpgradeModemPeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, "enderwire_peripheral_connected_pocket");

    public EnderwirePeripheralConnectedPocket() {
        super(ID, TranslationUtil.pocket("enderwire_peripheral_connected"), Blocks.ENDERWIRE_PERIPHERAL_CONNECTOR);
    }

    @Override
    protected EnderwireUpgradeModemPeripheral getPeripheral(IPocketAccess pocket) {
        return new EnderwireUpgradeModemPeripheral(pocket);
    }

    @Override
    public void update(@NotNull IPocketAccess access, @Nullable IPeripheral peripheral) {
        if (peripheral instanceof EnderwireUpgradeModemPeripheral) {
            if (!((EnderwireUpgradeModemPeripheral) peripheral).isInitialized())
                ((EnderwireUpgradeModemPeripheral) peripheral).initialize();
        }
    }
}