package site.siredvin.progressiveperipherals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tags.ItemTags;

import java.nio.file.Path;

public class ItemTagsProvider extends TagsProvider<Item> {

    public ItemTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Registry.ITEM, ProgressivePeripherals.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ItemTags.IRREALIUM_STRUCTURE_CASING).add(
                Blocks.IRREALIUM_MACHINERY_CASING.get().asItem(),
                Blocks.IRREALIUM_MACHINERY_GLASS.get().asItem(),
                Blocks.IRREALIUM_MACHINERY_IO_PORT.get().asItem(),
                Blocks.IRREALIUM_MACHINERY_STORAGE.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get().asItem(),
                Blocks.CREATIVE_IRREALIUM_DECRYPTOR.get().asItem()
        );
        this.tag(ItemTags.IRREALIUM_STRUCTURE_CORNER).add(
                Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get().asItem(),
                Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get().asItem(),
                Blocks.IRREALIUM_MACHINERY_CASING.get().asItem()
        );
    }

    @Override
    protected @NotNull Path getPath(ResourceLocation p_200431_1_) {
        return this.generator.getOutputFolder().resolve("data/" + p_200431_1_.getNamespace() + "/tags/items/" + p_200431_1_.getPath() + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Item tags";
    }
}