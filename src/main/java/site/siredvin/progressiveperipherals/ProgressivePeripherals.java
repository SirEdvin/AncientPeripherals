package site.siredvin.progressiveperipherals;

import de.srendi.advancedperipherals.common.configuration.ConfigHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.SimpleModelTransform;
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
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.client.models.FlexibleRealityAnchorModelLoader;
import site.siredvin.progressiveperipherals.client.models.FlexibleStatueModelLoader;
import site.siredvin.progressiveperipherals.client.renderer.EnderwireLightEmitterTileRenderer;
import site.siredvin.progressiveperipherals.client.renderer.PedestalTileRenderer;
import site.siredvin.progressiveperipherals.client.renderer.RealityBreakthroughPointTileRenderer;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.EnderwireLightEmitterBlockColor;
import site.siredvin.progressiveperipherals.common.configuration.ConfigHolder;
import site.siredvin.progressiveperipherals.common.setup.*;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.EnderwirePeripheralConnectedTurtle;
import site.siredvin.progressiveperipherals.integrations.patchouli.AutomataRecipePage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionLeftPage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionPage;
import site.siredvin.progressiveperipherals.integrations.patchouli.LuaFunctionRightPage;
import site.siredvin.progressiveperipherals.server.SingleTickScheduler;
import site.siredvin.progressiveperipherals.utils.BiomeUtils;
import site.siredvin.progressiveperipherals.utils.Platform;
import vazkii.patchouli.client.book.ClientBookRegistry;

import java.util.HashSet;
import java.util.Map;

@Mod(ProgressivePeripherals.MOD_ID)
public class ProgressivePeripherals {

    public static final String MOD_ID = "progressiveperipherals";
    public static final Logger LOGGER = LogManager.getLogger("Progressive Peripherals");
    public static final ItemGroup TAB = new ItemGroup("progressiveperipherals") {
        @Override
        public @NotNull ItemStack makeIcon() {
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
        forgeBus.addListener(SingleTickScheduler::tick);

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
        RenderTypeLookup.setRenderLayer(Blocks.ENDERWIRE_LIGHT_EMITTER.get(), RenderType.cutoutMipped());
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.REALITY_BREAKTHROUGH_POINT.get(), RealityBreakthroughPointTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.ABSTRACTIUM_PEDESTAL.get(), PedestalTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.IRREALIUM_PEDESTAL.get(), PedestalTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.CREATIVE_ITEM_DUPLICATOR.get(), PedestalTileRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypes.ENDERWIRE_LIGHT_EMITTER.get(), EnderwireLightEmitterTileRenderer::new);

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

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEventListener {

        private static final ModelResourceLocation[] EXTRA_MODELS = new ModelResourceLocation[]{
                EnderwirePeripheralConnectedTurtle.NOT_CONNECTED_LEFT_MODEL,
                EnderwirePeripheralConnectedTurtle.NOT_CONNECTED_RIGHT_MODEL,
                EnderwirePeripheralConnectedTurtle.CONNECTED_LEFT_MODEL,
                EnderwirePeripheralConnectedTurtle.CONNECTED_RIGHT_MODEL,
        };

        @SubscribeEvent
        public static void registerBlockColors(final ColorHandlerEvent.Block event) {
            event.getBlockColors().register(new EnderwireLightEmitterBlockColor(), Blocks.ENDERWIRE_LIGHT_EMITTER.get());
        }

        @SubscribeEvent
        public static void onModelBakeEvent(ModelBakeEvent event) {
            // Loading extra models, because if they are not bound to any item or block it will be not loaded
            ModelLoader loader = event.getModelLoader();
            Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();

            for (ModelResourceLocation model : EXTRA_MODELS) {

                ResourceLocation location = new ResourceLocation(model.getNamespace(), model.getPath().replace('.', '/'));
                IUnbakedModel unbakedModel = loader.getModelOrMissing(location);
                unbakedModel.getMaterials(loader::getModelOrMissing, new HashSet<>());

                IBakedModel baked = unbakedModel.bake(loader, ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, location);
                if (baked != null) {
                    registry.put(model, baked);
                }
            }
        }
    }


}
