package site.siredvin.progressiveperipherals;

import de.srendi.advancedperipherals.common.configuration.ConfigHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.siredvin.progressiveperipherals.client.models.FlexibleRealityAnchorModelLoader;
import site.siredvin.progressiveperipherals.client.models.FlexibleStatueModelLoader;
import site.siredvin.progressiveperipherals.client.renderer.PedestalTileRenderer;
import site.siredvin.progressiveperipherals.client.renderer.RealityBreakthroughPointTileRenderer;
import site.siredvin.progressiveperipherals.common.configuration.ConfigHolder;
import site.siredvin.progressiveperipherals.common.events.BlockEvents;
import site.siredvin.progressiveperipherals.common.setup.*;
import site.siredvin.progressiveperipherals.extra.network.events.NetworkEventTool;
import site.siredvin.progressiveperipherals.integrations.patchouli.AutomataRecipePage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionLeftPage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionPage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionRightPage;
import site.siredvin.progressiveperipherals.utils.BiomeUtils;
import site.siredvin.progressiveperipherals.utils.Platform;
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
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::modelSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::interModComms);
        modEventBus.addListener(ConfigHandler::configEvent);
        modEventBus.addListener(ConfigHandler::reloadConfigEvent);
        // Forge events
        forgeBus.addListener(this::biomeModification);
        forgeBus.addListener(NetworkEventTool::performCleanup);
        // Mod event listeners
        BlockEvents.subscribe(forgeBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC);
        Registration.register();
        // Register ourselves for server and other game events we are interested in
        forgeBus.register(this);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CCRegistration.register();
            AdditionalRecipes.register();
            ConfiguredFeatures.register();
            BiomeUtils.setupOverworldBiomesSet();
        });
    }

    @SubscribeEvent
    public void modelSetup(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ProgressivePeripherals.MOD_ID, "flexible_reality_anchor"), new FlexibleRealityAnchorModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(ProgressivePeripherals.MOD_ID, "flexible_statue"), new FlexibleStatueModelLoader());
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Blocks.FLEXIBLE_REALITY_ANCHOR.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(Blocks.FLEXIBLE_STATUE.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(Blocks.IRREALIUM_MACHINERY_GLASS.get(), RenderType.translucent());
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.REALITY_BREAKTHROUGH_POINT.get(), RealityBreakthroughPointTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.ABSTRACTIUM_PEDESTAL.get(), PedestalTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.IRREALIUM_PEDESTAL.get(), PedestalTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.CREATIVE_ITEM_DUPLICATOR.get(), PedestalTileRenderer::new);

        if (ModList.get().isLoaded("patchouli")) {
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "automata"), AutomataRecipePage.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "lua_function"), LuaFunctionPage.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "lua_function_left_page"), LuaFunctionLeftPage.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(MOD_ID, "lua_function_right_page"), LuaFunctionRightPage.class);
        }
    }

    @SubscribeEvent
    public void interModComms(InterModEnqueueEvent event) {
        Platform.maybeLoadIntegration("theoneprobe", "top.TOPPlugin")
                .ifPresent(handler -> InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> handler));
    }

    @SubscribeEvent
    public void biomeModification(final BiomeLoadingEvent event) {
        if (BiomeUtils.isOverworldBiome(event) && event.getCategory() != Biome.Category.OCEAN)
            event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> ConfiguredFeatures.REALITY_BREAKTHROUGH_POINT);
    }

}
