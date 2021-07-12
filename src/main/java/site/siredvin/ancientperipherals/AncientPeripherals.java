package site.siredvin.ancientperipherals;

import de.srendi.advancedperipherals.common.configuration.ConfigHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.siredvin.ancientperipherals.common.configuration.ConfigHolder;
import site.siredvin.ancientperipherals.common.models.FlexibleRealityAnchorModelLoader;
import site.siredvin.ancientperipherals.common.setup.Blocks;
import site.siredvin.ancientperipherals.common.setup.CCRegistration;
import site.siredvin.ancientperipherals.common.setup.Registration;

@Mod(AncientPeripherals.MOD_ID)
public class AncientPeripherals {

    public static final String MOD_ID = "ancientperipherals";
    public static final Logger LOGGER = LogManager.getLogger("Ancient Peripherals");
    public static final ItemGroup TAB = new ItemGroup("ancientperipherals") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.REALITY_FORGER.get());
        }
    };

    public AncientPeripherals() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::modelSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(ConfigHandler::configEvent);
        modEventBus.addListener(ConfigHandler::reloadConfigEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC);
        Registration.register();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CCRegistration::register);
    }

    @SubscribeEvent
    public void modelSetup(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(AncientPeripherals.MOD_ID, "flexible_reality_anchor"), new FlexibleRealityAnchorModelLoader());
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Blocks.FLEXIBLE_REALITY_ANCHOR.get(), RenderType.translucent());
    }
}
