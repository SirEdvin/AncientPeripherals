package site.siredvin.progressiveperipherals;

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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.siredvin.progressiveperipherals.common.configuration.ConfigHolder;
import site.siredvin.progressiveperipherals.client.models.FlexibleRealityAnchorModelLoader;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.CCRegistration;
import site.siredvin.progressiveperipherals.common.setup.AdditionalRecipes;
import site.siredvin.progressiveperipherals.common.setup.Registration;
import site.siredvin.progressiveperipherals.integrations.patchouli.AutomataRecipePage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionLeftPage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionPage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionRightPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

@Mod(ProgressivePeripherals.MOD_ID)
public class ProgressivePeripherals {

    public static final String MOD_ID = "progressiveperipherals";
    public static final Logger LOGGER = LogManager.getLogger("Progressive Peripherals");
    public static final ItemGroup TAB = new ItemGroup("progressiveperipherals") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.REALITY_FORGER.get());
        }
    };

    public ProgressivePeripherals() {
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
        event.enqueueWork(() -> {
            CCRegistration.register();
            AdditionalRecipes.register();
        });
    }

    @SubscribeEvent
    public void modelSetup(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ProgressivePeripherals.MOD_ID, "flexible_reality_anchor"), new FlexibleRealityAnchorModelLoader());
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Blocks.FLEXIBLE_REALITY_ANCHOR.get(), RenderType.translucent());
        if (ModList.get().isLoaded("patchouli")) {
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "automata"), AutomataRecipePage.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "lua_function"), LuaFunctionPage.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "lua_function_left_page"), LuaFunctionLeftPage.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "lua_function_right_page"), LuaFunctionRightPage.class);
        }
    }
}
