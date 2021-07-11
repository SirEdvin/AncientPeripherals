package site.siredvin.ancientperipherals;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.siredvin.ancientperipherals.common.configuration.ConfigHolder;
import site.siredvin.ancientperipherals.common.setup.CCRegistration;
import site.siredvin.ancientperipherals.common.setup.Registration;

@Mod(AncientPeripherals.MOD_ID)
public class AncientPeripherals {

    public static final String MOD_ID = "ancientperipherals";
    public static final Logger LOGGER = LogManager.getLogger("Ancient Peripherals");
    public static final ItemGroup TAB = new ItemGroup("ancientperipheralstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.DIRT);
        }
    };

    public AncientPeripherals() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC);
        Registration.register();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CCRegistration::register);
    }
}
