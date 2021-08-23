package site.siredvin.progressiveperipherals.integrations.computercraft.pocket;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireNetworkManagerPeripheral;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

public class EnderwireNetworkManagementPocket extends BasePocket<EnderwireNetworkManagerPeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(ProgressivePeripherals.MOD_ID, "enderwire_network_manager_pocket");

    public EnderwireNetworkManagementPocket() {
        super(ID, TranslationUtil.pocket("enderwire_network_manager"), Items.ENDERWIRE_NETWORK_MANAGER);
    }

    @Override
    protected EnderwireNetworkManagerPeripheral getPeripheral(IPocketAccess pocket) {
        return new EnderwireNetworkManagerPeripheral(pocket);
    }

    @Override
    public boolean onRightClick(@NotNull World world, @NotNull IPocketAccess access, @Nullable IPeripheral peripheral) {
        Entity entity = access.getEntity();
        if (entity instanceof PlayerEntity) {
            return entity.getPose() != Pose.CROUCHING;
        }
        return super.onRightClick(world, access, peripheral);
    }
}
